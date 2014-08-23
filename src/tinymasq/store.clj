(ns tinymasq.store
  "host lookup"
  (:require  
    [tinymasq.config :refer (tiny-config)]
    [clojure-watch.core :refer  [start-watch]])
  )

(def records (ref {:bla.com. [1 2 3 4]}))

(def server-conn {:pool {} :spec (tiny-config :redis)}) 

(defmacro wcar* [& body] 
  `(car/wcar server-conn ~@body))

(defn lookup [host]
  (@records (keyword host)))

