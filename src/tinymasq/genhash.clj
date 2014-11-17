(ns tinymasq.genhash
  "generating bcrypt hash"
   (:gen-class true)
   (:require
     (cemerick.friend [credentials :as creds]))
 )

(defn -main
   "generate a bcrypt hash" 
   [pass & args]
  (println (creds/hash-bcrypt pass))
  )
