(defproject tinymasq "0.1.2-ffms-1"
  :description "A mallable DNS forwarder"
  :url "https://github.com/narkisr/tinymasq"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
      [org.clojure/clojure "1.6.0"]
      [com.taoensso/carmine "2.12.1"]
      [dnsjava/dnsjava "2.1.7"]
      [com.taoensso/timbre "3.3.1"]
      [org.clojure/core.incubator "0.1.3"]
      [org.clojure/core.cache "0.6.4"]
      [org.clojure/core.async "0.2.374"]
      ; http api
      [ring/ring-ssl "0.2.1"]
      [ring-middleware-format "0.7.0"]
      [ring/ring-jetty-adapter "1.4.0"]
      [com.cemerick/friend "0.2.1"]
      [ring "1.4.0"]
      [compojure "1.4.0" :exclusions  [ring/ring-core]]]

  :exclusions [org.clojure/clojure org.clojure/core.cache]
  :profiles {
    :dev {
      :dependencies [[midje "1.8.3"]]
      :plugins [
          [lein-set-version "0.3.0"]
          [lein-midje "3.1.3"]
          [lein-ancient "0.5.5"]
          [lein-tar "2.0.0"]
          [lein-tag "0.1.0"]
      ]
    }
  }

  :set-version {
    :updates [{:path "src/tinymasq/core.clj" :search-regex #"\"\d+\.\d+\.\d+\""}]
  }


  :aliases {
      "genhash" ["run" "-m" "tinymasq.genhash"]
  }

  :aot [tinymasq.core tinymasq.import tinymasq.genhash tinymasq.bench]

  :main tinymasq.core

)
