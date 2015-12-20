(ns tinymasq.test.store
  (:use midje.sweet)
  (:import java.lang.AssertionError)
  (:require
    [tinymasq.store :refer (add-host del-host update-host get-host)]))

(fact "legal hosts storage"
   (del-host "password" "foo")  => (throws Exception)
   (add-host "fooowner/secretausce" "foo" "1.2.3.4"   ) => "OK"
   (add-host "bob/secretsauce" "bar" "4.5.6.7" "Welcome to Bob's Country Bunker!" ) => OK
   (get-host "foo") => "1.2.3.4"
   (update-host "fooowner/secretausce"  "foo" "1.2.3.5") => "OK"
   (update-host "fooowner/secretausce"  "foo" "1.2.3.5"   "This is the awesome baz-service") => "OK"
   (get-host "foo") => "1.2.3.5"
   (list-hosts) => OK
  )

(fact "faulty hosts storage"
   (del-host "foo")
   (add-host "foo" "1.2.3.4") => "OK"
   (add-host "foo" "1.2.3.4") => (throws Exception)
   (add-host "somepwf" "baz.blerk" "8.8.8.8") => (throws Exception) ;subdomains are not permitted
   (update-host "foo" "1.2.3.5") => "OK"
   (del-host "bar")
   (update-host "bar" "1.2.3.5") => (throws Exception))
