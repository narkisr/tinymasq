(ns tinymasq.test.store
  (:use midje.sweet)  
  (:import java.lang.AssertionError)
  (:require 
    [tinymasq.store :refer (add-host del-host update-host get-host)]))

(fact "legal hosts storage"
   (del-host "foo")
   (add-host "foo" "1.2.3.4") => "OK"
   (get-host "foo") => "1.2.3.4"
   (update-host "foo" "1.2.3.5") => "OK"
   (get-host "foo") => "1.2.3.5"
  )

(fact "faulty hosts storage"
   (del-host "foo")
   (add-host "foo" "1.2.3.4") => "OK"
   (add-host "foo" "1.2.3.4") => (throws Exception)
   (update-host "foo" "1.2.3.5") => "OK"
   (del-host "bar")
   (update-host "bar" "1.2.3.5") => (throws Exception))
