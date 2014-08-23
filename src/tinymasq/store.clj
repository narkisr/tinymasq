(ns tinymasq.store
  "host lookup"
  (:require  
    [clojure.string :refer (split)]
    [taoensso.carmine :as car :refer  (wcar)]
    [tinymasq.config :refer (tiny-config)]
    )
  )

(def records (ref {:bla.com. [1 2 3 4]}))

(def server-conn {:pool {} :spec (tiny-config :redis)}) 

(defmacro wcar* [& body] 
  `(car/wcar server-conn ~@body))

(defn into-bytes
  "converting an ip 1.2.3.4 into bytes array [1 2 3 4]"
   [ip]
   (when ip
     (byte-array (mapv #(.byteValue (Integer. %)) (split ip #"\.")))))

(defn lookup [host]
  (into-bytes (wcar* (car/get host))))

(defn add-host 
  "Adding hostname -> ip" 
  [host ip]
  (wcar* (car/set host ip)))

;; (add-host "bla.com." "255.127.1.10")

