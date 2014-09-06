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
    [compojure.core :refer (defroutes routes)] 
    [swag.model :refer (wrap-swag)]
    [ring.middleware.format :refer (wrap-restful-format)]
    [swag.core :refer (GET- POST- PUT- DELETE- defroutes- errors)]
    )
  )

(defroutes- hosts {:path "/hosts" :description "Hosts manipulation"})

(defn app []
  (-> 
    (routes hosts)
    (wrap-swag)
    (wrap-restful-format :formats [:json-kw :edn :yaml-kw :yaml-in-html])))

