(ns tinymasq.test.store
  (:use midje.sweet)  
  (:require 
    [tinymasq.store :refer (add-host del-host update-host get-host)]))

(fact "adding hosts" filters
   (del-host "foo")
   (add-host "foo" "1.2.3.4") => "OK"
   (get-host "foo") => "1.2.3.4"
   (update-host "foo" "1.2.3.5") => "OK"
   (get-host "foo") => "1.2.3.5"
  )


