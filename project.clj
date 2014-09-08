(defproject tinymasq "0.0.1"
  :description "A malable DNS forwarder"
  :url "https://github.com/narkisr/tinymasq"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
      [org.clojure/clojure "1.6.0"]
      [aleph "0.3.3"]
      [com.taoensso/carmine "2.6.2"] 
      [dnsjava/dnsjava "2.1.6"]
      [com.taoensso/timbre "2.6.3"]
      ; http api
      [swag "0.2.7"]
      [ring/ring-ssl "0.2.1"]
      [ring-middleware-format "0.3.0"]
      [ring/ring-jetty-adapter "1.2.0"]
      [ring "1.3.0"]
      [compojure "1.1.8" :exclusions  [ring/ring-core]]]
  
  :main tinymasq.core

)
