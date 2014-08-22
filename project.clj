(defproject nadir "0.0.1"
  :description "A malable DNS forwarder"
  :url ""
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
      [org.clojure/clojure "1.5.1"]
      [aleph "0.3.3"]
      [dnsjava/dnsjava "2.1.6"] 
   ]
  
  :main nadir.core

)
