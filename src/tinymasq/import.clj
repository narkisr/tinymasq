(ns tinymasq.import
  "importing existing hosts file"
  (:gen-class true)
  (:require 
    [clojure.string :refer (split)] 
    [tinymasq.store :refer (add-host)]))


(def local #{"127.0.0.1" "127.0.1.1"})

(defn slurp-hosts 
  "read hosts file" 
  [path]
  (map #(split % #"\s+") (split (slurp path) #"\n")))

(defn import-hosts 
  "import hosts file into redis"
  [file]
  (doseq [[ip f s] (slurp-hosts file)]
    (when (and (re-matches #"\d+.\d+.\d+.\d+" ip) (not (local ip)))
      (add-host f ip)
      (println "imported " f ip))))

(defn -main [f & args] (import-hosts f))
