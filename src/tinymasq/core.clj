(comment 
  Tinymasq, Copyright 2013 Ronen Narkis, narkisr.com
  Licensed under the Apache License,
  Version 2.0  (the "License") you may not use this file except in compliance with the License. 
  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0                     
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,                                      
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.)

(ns tinymasq.core
  (:gen-class true)
  (:require 
    [clojure.string :refer (split)]
    [ring.adapter.jetty :refer (run-jetty)] 
    [tinymasq.ssl :refer (generate-store)]
    [tinymasq.api :refer (app)]
    [taoensso.timbre :as timbre :refer (refer-timbre set-level! set-config!)]
    [clojure.core.async :refer (thread go >! >!! <!! <! alts! dropping-buffer go-loop chan)]
    [clojure.java.io :refer (file)]
    [tinymasq.config :refer (tiny-config ssl-conf log-conf)]
    [tinymasq.store :refer (get-host)])
  (:import 
    (org.xbill.DNS Message Section Name Type DClass Record) 
    (java.net InetAddress DatagramPacket DatagramSocket)))

(refer-timbre)

(def udp-server (atom nil))

(defn localhost [] (. InetAddress getLocalHost))

(defn create-udp-server []
  (DatagramSocket. (tiny-config :udp-port)))

(defn start-udp-server []
  (reset! udp-server (create-udp-server)))

(defn stop-udp-server []
  (.close @udp-server))

(defn packet [bs] 
  (DatagramPacket. bs (alength bs)))

(defn record-of
  [host ip]
  (let [n (Name/fromString host) ip (byte-array (map byte ip))]
    (Record/newRecord n Type/A DClass/IN (tiny-config :ttl) ip)))

(defn into-bytes
  "converting an ip 1.2.3.4 into bytes array [1 2 3 4]"
   [ip]
   (when ip
     (byte-array (mapv #(.byteValue (Integer. %)) (split ip #"\.")))))

(defn normalized-host 
   "removes last . from host" 
   [host]
  (reduce str (butlast host)))

(def lookups (chan (dropping-buffer 100)))
(def answers (chan (dropping-buffer 100)))

(defn accept-loop 
  []
  (go 
    (while true
      (let [pkt (packet (byte-array 1024))]
        (.receive @udp-server pkt)
        (>! lookups pkt) 
        ))))

(defn process-loop 
  "Async processing of packets" 
  []
  (go 
    (while true
      (let [pkt (<! lookups) message (Message. (.getData pkt)) 
            record (.getQuestion message) host (.toString (.getName record) false)
            ip (get-host (normalized-host host))]
        (when ip
          (.addRecord message (record-of host (into-bytes ip)) Section/ANSWER))
        (.setData pkt (.toWire message))
        (>! answers [pkt host ip])
        ))))

(defn reply-loop 
  "Sends back reponses" 
  []
  (go
    (while true
      (let [[pkt host ip] (<! answers)] 
        (.send @udp-server pkt)
        (trace "Query result for" host "is" ip)))))

(defn default-key
  "Generates a default keystore if missing"
  []
  (when-not (.exists (file (ssl-conf :keystore)))
    (info "generating a default keystore")
    (generate-store (ssl-conf :keystore) (ssl-conf :password))))

(defn setup-logging 
  "Sets up logging configuration"
  []
  (set-config! [:shared-appender-config :spit-filename] (log-conf :path)) 
  (set-config! [:appenders :spit :enabled?] true) 
  (set-level! (log-conf :level)))


(def version "0.2.0")

(defn -main [& args]
  (setup-logging)
  (start-udp-server)
  (default-key)
  (run-jetty (app)
             {:port 8081 :join? false
              :ssl? true 
              :keystore (ssl-conf :keystore)
              :key-password  (ssl-conf :password)
              :ssl-port 8444})
  (info "Tinymasq" version "is running")
  (reply-loop)
  (process-loop) 
  (accept-loop))

