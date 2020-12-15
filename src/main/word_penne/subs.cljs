(ns word-penne.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::current-route
 (fn [db _]
   (get db :route {:handler :word-penne.pages.home/home})))

(re-frame/reg-sub
 ::show-navigation
 (fn [db _]
   (:show-navigation db)))
