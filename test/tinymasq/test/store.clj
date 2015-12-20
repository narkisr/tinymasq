(ns tinymasq.test.store
  (:use midje.sweet)
  (:import java.lang.AssertionError)
  (:require
    [tinymasq.store :refer (add-host del-host update-host get-host list-hosts)]))

(fact "legal hosts storage"
   (del-host "magictoken" "foo")  => (throws Exception)
   (add-host "fooowner/secretausce" "foo" "1.2.3.4"   ) => "OK"
   (update-host "fooowner/secretausce"  "foo" "1.2.3.5") => "OK"
   (update-host "wrongtoken"  "foo" "1.2.3.5") => nil
   (add-host "bob/secretsauce" "bar" "4.5.6.7" "Welcome to Bob's Country Bunker!" ) => "OK"
   (get-host "magictoken" "foo") => "1.2.3.4"

   (update-host "fooowner/secretausce"  "foo" "1.2.3.5"   "This is the awesome baz-service") => "OK"
   (get-host "foo") => "1.2.3.5"
   (list-hosts) => "OK"
  )

(fact "faulty hosts storage"
   (del-host "bob/secretsauce"  "foo")
   (add-host "bob/secretsauce" "foo" "1.2.3.4") => "OK"
   (add-host "bob/secretsauce" "foo" "1.2.3.4") => (throws Exception)
   (add-host "bob/secretsauce" "baz.blerk" "8.8.8.8") => "OK" ; subdomains are not permitted but this is the storage layer so we should not care.
   (update-host "bob/secretsauce" "foo" "1.2.3.5") => "OK"
   (del-host  "bob/secretsauce" "bar")
   (update-host "bob/secretsauce" "bar" "1.2.3.5") => (throws Exception))
