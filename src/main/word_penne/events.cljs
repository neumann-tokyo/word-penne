(ns word-penne.events
  (:require [re-frame.core :as re-frame]
            ;; [bidi.bidi :as bidi]
            [word-penne.db :as db]
            [word-penne.fx :as fx]
            [word-penne.subs :as subs]
            ;; [word-penne.routes :refer [routes]]
            [word-penne.firebase.auth :as firebase-auth]))

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

(re-frame/reg-event-fx
 ::signout
 (fn [_ _]
   (-> (firebase-auth/auth)
       (.signOut)
       (.then (fn []
                (re-frame/dispatch [::set-current-user nil])
                (re-frame/dispatch [::navigate :word-penne.pages.auth/signin])))
       (.catch (fn [_]
                 (re-frame/dispatch [::navigate :word-penne.pages.home/home]))))))

(re-frame/reg-event-fx
 ::fetch-cards
 (fn [_ _]
   {:fx/firebase-load-cards {:user-id @(re-frame/subscribe [::subs/current-user])}}))

(re-frame/reg-event-db
 ::set-cards
 (fn [db [_ res]]
   (assoc db :cards res)))
