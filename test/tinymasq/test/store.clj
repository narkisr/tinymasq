(ns tinymasq.test.store
  (:use midje.sweet)
  (:import java.lang.AssertionError)
  (:require
    [tinymasq.store :refer (del-host update-host get-host list-hosts access-permitted?)]))

(fact "access rights "
   ;(del-host "magictoken" "foo")  => (throws Exception)
   (access-permitted? "egal" "notthere") => true   ;;access is permitted if it does not exist yet
   (update-host "fooowner-secretausce" "foo" "1.2.3.4"   ) => "OK"
   (access-permitted? "fooowner-secretausce" "foo") => true  ;;access permitted if it exists and auth token correct
   (access-permitted? "barowner-secretausce" "foo") => false   ;;access denied if it exists and auth token incorrect
   (get-host "foo") => "1.2.3.4"
  )

(fact "changes"
   (update-host "fooowner/secretausce"  "foo" "4.3.2.1") => "OK"
   (update-host "wrongtoken"  "foo" "1.2.3.5") => nil   ;;access denied if it exists and auth token incorrect
   (update-host "bob/secretsauce" "bar" "4.5.6.7" "Welcome to Bob's Country Bunker!" ) => "OK"
   (get-host "magictoken" "foo") => "4.3.2.1"


   (update-host "fooowner/secretausce"  "foo" "1.2.3.5"   "This is the awesome baz-service") => "OK"
   (get-host "foo") =>  (throws Exception)
   (list-hosts) => "OK"
  )

(fact "faulty hosts storage"
   (del-host "bob/secretsauce"  "foo")
   (update-host "bob/secretsauce" "foo" "1.2.3.4") => "OK"
   (update-host "bob/secretsauce" "foo" "1.2.3.4") => (throws Exception)
   (update-host "bob/secretsauce" "baz.blerk" "8.8.8.8") => "OK" ; subdomains are not permitted but this is the storage layer so we should not care.
   (update-host "bob/secretsauce" "foo" "1.2.3.5") => "OK"
   (del-host  "bob/secretsauce" "bar")
   (update-host "bob/secretsauce" "bar" "1.2.3.5") => (throws Exception))
