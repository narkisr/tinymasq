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

(ns tinymasq.api
  "Add/Remove hosts"
  (:require 
    [tinymasq.store :refer (add-host update-host del-host get-host)]
    [compojure.core :refer (defroutes routes)] 
    [ring.middleware.ssl :refer (wrap-ssl-redirect)]
    [swag.model :refer (wrap-swag)]
    [ring.middleware.format :refer (wrap-restful-format)]
    [swag.core :refer (GET- POST- PUT- DELETE- defroutes- errors)]
    [compojure.core :refer (GET POST PUT DELETE)]
    )
  )

(defroutes hosts 
  (POST "/hosts" {{hostname :hostname ip :ip} :params}
    (add-host hostname ip)
    {:status 200 :body "host added"})
  (PUT "/hosts" {{hostname :hostname ip :ip} :params}
    (add-host hostname ip)
    {:status 200 :body "host updated"})
  (DELETE "/hosts" {{hostname :hostname} :params}
    (del-host hostname)
    {:status 200 :body "host removed"})
  (GET "/hosts" {{hostname :hostname} :params}
    (if-let [ip (get-host hostname)]
      {:status 200 :body {:ip ip}}
      {:status 404 :body "hostname not found"}
      ))
  )

(defn app []
  (-> 
    (routes hosts)
    (wrap-ssl-redirect :ssl-port 8444)
    (wrap-restful-format :formats [:json-kw :edn :yaml-kw :yaml-in-html])))

