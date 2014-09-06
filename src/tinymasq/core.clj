(comment 
  Celestial, Copyright 2013 Ronen Narkis, narkisr.com
  Licensed under the Apache License,
  Version 2.0  (the "License") you may not use this file except in compliance with the License. 
  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0                     
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,                                      
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.)

(ns tinymasq.core
  (:require 
    [ring.adapter.jetty :refer (run-jetty)] 
    [tinymasq.ssl :refer (generate-store)]
    [tinymasq.api :refer (app)]
    [taoensso.timbre :as timbre :refer (refer-timbre)]
    [clojure.java.io :refer (file)]
    [tinymasq.config :refer (tiny-config ssl-conf)]
    [tinymasq.store :refer (lookup)])
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

(defn read-write-loop 
  []
  (while true
    (let [pkt (packet (byte-array 1024))]
      (.receive @udp-server pkt)
      (let [message (Message. (.getData pkt)) record (.getQuestion message)
            host (.toString (.getName record) false) ip (lookup host)]
        (when ip
          (.addRecord message (record-of host ip) Section/ANSWER))
        (.setData pkt (.toWire message))
        (.send @udp-server pkt)))))

(defn default-key
  "Generates a default keystore if missing"
  []
  (when-not (.exists (file (ssl-conf :keystore)))
    (info "generating a default keystore")
    (generate-store (ssl-conf :keystore) (ssl-conf :password))))

(defn -main []
  (start-udp-server)
  (default-key)
  (run-jetty (app)
     {:port 8081 :join? false
        :ssl? true 
        :keystore (ssl-conf :keystore)
        :key-password  (ssl-conf :password)
        :ssl-port 8444})
  (read-write-loop))