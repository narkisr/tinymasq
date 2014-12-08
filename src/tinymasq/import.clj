(ns tinymasq.import
  "importing existing hosts file"
  (:gen-class true)
  (:require 
    [clojure.string :refer (split)] 
    [tinymasq.store :refer (add-host get-host del-host update-host)]))


(def local #{"127.0.0.1" "127.0.1.1"})

(defn slurp-hosts 
  "read hosts file" 
  [path]
  (map #(split % #"\s+") (split (slurp path) #"\n")))

(defn update-or-add 
  "Adding or updateing an ip" 
  [ip f]
  (if (get-host f) 
    (do
      (update-host f ip)
      (println "re-imported " f ip)) 
    (do 
      (add-host f ip)
      (println "imported " f ip))))

(defn import-hosts 
  "import hosts file into redis"
  [file]
  (doseq [[ip f s] (slurp-hosts file)]
    (when (and (re-matches #"\d+.\d+.\d+.\d+" ip) (not (local ip)))
      (if (re-matches #".*\..*\..*" f)
        (update-or-add ip f)
        (update-or-add ip s)))))

(defn -main [f & args] (import-hosts f))
