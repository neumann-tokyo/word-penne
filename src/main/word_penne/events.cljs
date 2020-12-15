(ns word-penne.events
  (:require [re-frame.core :as re-frame]
            [word-penne.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-current-route
 (fn [db [_ route]]
   (assoc db :route route)))

(re-frame/reg-event-db
 ::set-show-navigation
 (fn [db [_ show]]
   (assoc db :show-navigation show)))
