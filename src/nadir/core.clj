(ns nadir.echo
  (:import 
    org.xbill.DNS.Message (java.net InetAddress DatagramPacket DatagramSocket)))
 
(def udp-server (ref nil))
 
(def port 1234)
 
(defn localhost [] (. InetAddress getLocalHost))
 
(defn create-udp-server []
  (DatagramSocket. port))
 
(defn start-udp-server []
  (dosync (ref-set udp-server (create-udp-server))))
 
(defn stop-udp-server []
  (.close @udp-server))
 
(start-udp-server)
 
(defn empty-message [n] 
  (new DatagramPacket (byte-array n) n))
 
(defn -main []
  (while true
    (let [pkt (empty-message 1024)]
      (.receive @udp-server pkt)
      (let [record (.getQuestion (Message. (.getData pkt)))
            label (.toString (.getName record) true)]
      (println label))))
  )
