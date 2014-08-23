(ns tinymasq.core
  (:require 
    [tinymasq.config :refer (tiny-config)]
    [tinymasq.store :refer (lookup)])
  (:import 
    (org.xbill.DNS Message Section Name Type DClass Record) 
    (java.net InetAddress DatagramPacket DatagramSocket)))

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

(defn -main []
  (start-udp-server)
  (read-write-loop))
