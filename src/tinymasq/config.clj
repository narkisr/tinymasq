(ns tinymasq.config
  "configuration"
  (:require 
    [clojure.edn :as edn]
    [clojure.java.io :as io]))

(def tiny-config 
  (-> "config/tiny.edn" slurp edn/read-string))

