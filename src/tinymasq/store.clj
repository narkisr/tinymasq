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
  (if (= res "OK")
    true
    (throw (Exception. (<< "Failed to ~{action} ~{host}")))))

(defn- get-host-details
  "get host details, given auth-token and name"
  [auth host]
  (let [host-details (wcar* (car/get host))
        [rec-auth rec-ip rec-description] host-details]
    (if (compare auth rec-auth)
      host-details
      nil)))

(defn get-host
  "get host ip, given name and auth-token"
  [auth host]
  (let [[rec-auth rec-ip rec-description]  (get-host-details auth host)]
    rec-ip))


(defn add-host
  "Adding hostname -> ip, auth is the authentication-token
  that is required for follow up operations, description is an optional text which may be displayed when listing the service"
  [auth host ip & [description]]
  {:post [(assert-op "add" host %)]}
  (wcar* (car/set host
                  (str (list auth ip description))
                  "NX" )))

(defn- access-permitted?
  "Check whether change to a host is permitted.
  This is true if the host does not exist or the correct auth-token is supplied"
  [auth host]
  (let [details (get-host-details auth host)]
    (if details
      (if (compare auth (second details)) ;;
        true
        nil)
      nil)))


(defn update-host
  "Update hostname -> ip"
  [auth host ip & [description]]
  {:post [(assert-op "add" host %)]}
  (if (access-permitted? auth host)
    (wcar* (car/set host
                    (str (list auth ip description))
                    "XX"))
    nil))

(defn del-host
  "Deleting a host"
  [auth host]
  (if (access-permitted? auth host)
    (wcar* (car/del host))
    nil))


(defn list-hosts
  []
  "list of known hosts"
  nil)


(comment
  Tinymasq, Copyright 2013 Ronen Narkis, narkisr.com
  Licensed under the Apache License,
  Version 2.0  (the "License") you may not use this file except in compliance with the License.
  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.)
