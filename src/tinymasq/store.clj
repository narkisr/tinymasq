(ns tinymasq.store
  "host lookup"
  (:require
   [taoensso.timbre :as timbre :refer (refer-timbre)]
   [clojure.core.strint :refer (<<)]
   [taoensso.carmine :as car :refer  (wcar)]
   [tinymasq.config :refer (tiny-config)]
   [clojure.edn :as edn]))


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
  [host]
  (let [host-details (edn/read-string (wcar* (car/get host)))
        [rec-auth rec-ip rec-description] host-details]
    (if (compare auth rec-auth)
      host-details
      nil)))

(defn get-host
  "get host ip, given name and auth-token"
  [host]
  (let [[rec-auth rec-ip rec-description]  (get-host-details host)]
    rec-ip))


(defn access-permitted?
  "Check whether change to a host is permitted.
  This is true if the host does not exist or the correct auth-token is supplied"
  [auth host]
  (let [details (get-host-details auth host)]
    (if (some? details)
      (if (= 0 (compare auth (first details)))
        true
        false)
      true)))



(defn update-host
  "Update (or create) host -> ip entry.
  auth is the authentication-token that is required for follow up operations,
  description is an optional text which may be displayed when listing the service"
  [auth host ip & [description]]
  {:post [(assert-op "add" host %)]}
  (if (access-permitted? auth host)
    (wcar* (car/set host
                    (prn-str (list auth ip description))))
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

