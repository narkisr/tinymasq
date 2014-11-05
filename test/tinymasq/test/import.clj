(ns tinymasq.test.import
  (:use midje.sweet)  
  (:require 
    [tinymasq.store :refer (get-host del-host)]
    [tinymasq.import :refer (import-hosts)]))

(fact "import conflicts" :integration :import
   (del-host "master.debian.org") 
   (import-hosts "resources/hosts") 
   (get-host "master.debian.org")  => "146.82.138.8"
  )

