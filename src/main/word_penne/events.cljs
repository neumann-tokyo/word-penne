(ns word-penne.events
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :as bidi]
            [word-penne.db :as db]
            [word-penne.fx :as fx]
            [word-penne.routes :refer [routes]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(defmulti on-navigate (fn [view _] view))
;; (defmethod on-navigate :todo-app.views/list [_ _]
;;   {:dispatch [::fetch-todos]})
;; (defmethod on-navigate :todo-app.views/edit [_ params]
;;   {:dispatch [::fetch-todo-by-id (:id params)]})
(defmethod on-navigate :default [_ _] nil)

(re-frame/reg-event-fx
 ::set-current-route
 (fn [{:keys [db]} [_ {:keys [handler route-params]
                       :as route}]]
   (merge {:db (assoc db :route route)}
          (on-navigate handler route-params))))

(re-frame/reg-event-fx
 ::navigate
 (fn [_ [_ view params]]
   {::fx/navigate {:view view
                   :params params}}))

(re-frame/reg-event-db
 ::set-current-user
 (fn [db [_ user]]
   (assoc db :user user)))
