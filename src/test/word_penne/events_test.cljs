(ns word-penne.events-test
  (:require [cljs.test :as t]
            ["node-fetch" :default fetch]
            [re-frame.core :as rf]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.firebase.init :refer [initialize-firebase]]))

;; clean up the database
(t/use-fixtures :each
  {:before
   (fn []
     (let [res (fetch "http://localhost:8081/emulator/v1/projects/word-penne/databases/(default)/documents" #js {:method "DELETE"})]
       (t/async done (.then res #(done)))))})

(t/deftest a-success-test
  (let [current-user (rf/subscribe [::subs/current-user])]
    (initialize-firebase)
    (rf/dispatch-sync [::events/initialize-db])
    (rf/dispatch-sync [::events/set-current-user
                       {:uid "uid1234"
                        :email "foo@example.com"
                        :photo-url "/images/account_circle-24px.svg"}])
    (t/is (= (:uid @current-user) "uid1234"))
    (t/is (= (:email @current-user) "foo@example.com"))
    (t/is (= (:photo-url @current-user) "/images/account_circle-24px.svg"))))
