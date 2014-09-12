(defproject tinymasq "0.0.2"
  :description "A malable DNS forwarder"
  :url "https://github.com/narkisr/tinymasq"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
      [org.clojure/clojure "1.6.0"]
      [com.taoensso/carmine "2.7.0"] 
      [dnsjava/dnsjava "2.1.6"]
      [com.taoensso/timbre "3.3.1"]
      ; http api
      [swag "0.2.7"]
      [ring/ring-ssl "0.2.1"]
      [ring-middleware-format "0.4.0"]
      [ring/ring-jetty-adapter "1.3.1"]
      [ring "1.3.1"]
      [compojure "1.1.9" :exclusions  [ring/ring-core]]]
  
  :exclusions [org.clojure/clojure]
  :profiles {
    :dev {
      :dependencies [[midje "1.6.3"]]
      :plugins [
          [lein-set-version "0.3.0"]
          [lein-midje "3.1.3"]
          [lein-ancient "0.5.5"]
          [lein-tar "2.0.0"]
          [lein-tag "0.1.0"]
      ]
    }
  }

  :aot [tinymasq.core]
 
  :main tinymasq.core

)
