(ns word-penne.events-test
  (:require [cljs.test :as t]
            ["node-fetch" :default fetch]
            [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [cljs.core.async :refer [go]]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]
            [word-penne.firebase.init :refer [initialize-firebase]]))

;; clean up the database
(t/use-fixtures :each
  {:before
   (fn []
     (let [res (fetch "http://localhost:8081/emulator/v1/projects/word-penne/databases/(default)/documents" #js {:method "DELETE"})]
       (t/async done (.then res #(done)))))})

(t/deftest create-a-card-test
  (rf-test/run-test-sync
   (let [current-user (re-frame/subscribe [::subs/current-user])
         cards (re-frame/subscribe [::subs/cards])]
     (t/async
      done
      (go

        ; initialize system
        (initialize-firebase)
        (re-frame/dispatch [::events/initialize-db])
        (accountant/configure-navigation!
         {:nav-handler (fn [path]
                         (re-frame/dispatch [::events/set-current-route (bidi/match-route routes path)]))
          :path-exists? (fn [path]
                          (boolean (bidi/match-route routes path)))
          :reload-same-path? true})
        (accountant/dispatch-current!)

        ; sign in user
        (re-frame/dispatch [::events/set-current-user
                            {:uid "uid1234"
                             :email "foo@example.com"
                             :photo-url "/images/account_circle-24px.svg"}])
        (t/is (= (:uid @current-user) "uid1234"))
        (t/is (= (:email @current-user) "foo@example.com"))
        (t/is (= (:photo-url @current-user) "/images/account_circle-24px.svg"))

        ; create a card
        (re-frame/dispatch [::events/create-card
                            {:values {"front-text" "make"
                                      "back-text" "作る"
                                      "comment" "一般的な作る"}}])
        (let [card (first @cards)]
          (t/is (= (:front-text card) "make"))
          (t/is (= (:back-text card) "作る"))
          (t/is (= (:comment card) "一般的な作る")))
        (done))))))

; TODO FIXME update a card うまくかけない...
