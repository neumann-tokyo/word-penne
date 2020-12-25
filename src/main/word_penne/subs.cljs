(ns word-penne.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::current-route
 (fn [db _]
   (get db :route {:handler :word-penne.pages.home/home})))

(re-frame/reg-sub
 ::current-user
 (fn [db _]
   (:user db)))

(re-frame/reg-sub
 ::cards
 (fn [db _]
   (:cards db)))

(re-frame/reg-sub
 ::selected-card
 (fn [db _]
   (:selected-card db)))
