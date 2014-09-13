(comment 
  Celestial, Copyright 2013 Ronen Narkis, narkisr.com
  Licensed under the Apache License,
  Version 2.0  (the "License") you may not use this file except in compliance with the License. 
  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0                     
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,                                      
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.)

(ns tinymasq.store
  "host lookup"
  (:require  
    [taoensso.timbre :as timbre :refer (refer-timbre)]
    [clojure.core.strint :refer (<<)]
    [taoensso.carmine :as car :refer  (wcar)]
    [tinymasq.config :refer (tiny-config)]))

(refer-timbre)

(def server-conn {:pool {} :spec (tiny-config :redis)}) 

(defmacro wcar* [& body] 
  `(car/wcar server-conn ~@body))

(defn assert-op 
  "Checking that the redis update/insert passed" 
  [action host res]
  (info res)
  (if (= res "OK")
    true
    (throw (Exception. (<< "Failed to ~{action} ~{host}")))))

(defn get-host 
  "get host ip"
  [host]
  (wcar* (car/get host)))

(defn add-host 
  "Adding hostname -> ip" 
  [host ip]
  {:post [(assert-op "add" host %)]}
  (wcar* (car/set host ip "NX")))

(defn update-host 
  "Update hostname -> ip" 
  [host ip]
  {:post [(assert-op "add" host %)]}
  (wcar* (car/set host ip "XX")))

(defn del-host 
  "Deleting a host" 
  [host]
  (wcar* (car/del host)))
