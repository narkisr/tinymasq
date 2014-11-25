(ns tinymasq.bench
  "benhmark tinymasq"
  (:gen-class true)
  (:require 
    [taoensso.timbre :as timbre :refer (refer-timbre)]
    [clojure.core.async :as async :refer (thread go >! >!! <!! <! alts! dropping-buffer go-loop chan)]
    [clojure.java.shell :refer [sh]]))

(refer-timbre)

(def succesful (chan (dropping-buffer 10)))

(def failed (chan (dropping-buffer 10)))

(def results (atom {:success 0 :fail 0}))

(defn success-loop 
  []
  (go
    (while true
      (let [c (<! succesful)] 
         (swap! results (fn [m] (assoc m :success (inc (m :success)))))
        ))))

(defn failed-loop 
  []
  (go
    (while true
      (let [c (<! failed)] 
        (swap! results (fn [m] (assoc m :fail (inc (m :fail)))))
        ))))

(defn dig-loop [c]
  (go 
    (while true
      (let [{:keys [exit out err]} (sh "dig" "@localhost" "foo.com" "-p" "1234")]
        (if (= 0 exit)
          (>! succesful 1)
          (>! failed 1))))))

(defn set-interval [callback ms] 
  (future (while true (do (Thread/sleep ms) (callback)))))

(defn bench 
  "Running dig and counting responses" 
  [c]
   (map dig-loop (range c)))

(defn add-shutdown []
  (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] (info "final suammry:" @results)))))

(defn -main [c & _] 
  (add-shutdown)
  (success-loop)
  (failed-loop)
  (set-interval #(info @results) 1000)
  (info (bench (Integer. c))))

