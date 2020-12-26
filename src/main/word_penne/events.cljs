(ns word-penne.events
  (:require [re-frame.core :as re-frame]
            [malli.core :as m]
            [cljs.pprint :refer [pprint]]
            ;; [bidi.bidi :as bidi]
            [word-penne.db :as db]
            [word-penne.fx :as fx]
            [word-penne.subs :as subs]
            [word-penne.config :as config]
            ;; [word-penne.routes :refer [routes]]
            ))

(def check-asserts? config/debug?)

(defn- check-and-throw
  "Throws an exception if `form` doesn't match the Spec `a-spec`."
  [a-spec form]
  (when (and check-asserts? (not (m/validate a-spec form)))
    (throw (js/Error. (str "spec check failed: " (with-out-str (pprint (m/explain a-spec form))))))))

(def validate-db (re-frame/after (partial check-and-throw db/t-db)))
(defn validate-args [a-spec]
  (re-frame/->interceptor
   :id :validate-args
   :before (fn [{{:keys [event]} :coeffects :as context}]
             (check-and-throw a-spec (last event))
             context)))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(defmulti on-navigate (fn [view _] view))
(defmethod on-navigate :word-penne.pages.home/home [_ _]
  {:dispatch [::fetch-cards]})
(defmethod on-navigate :word-penne.pages.cards/edit [_ params]
  {:dispatch [::fetch-card-by-uid (:id params)]})
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
 [(validate-args db/t-user)
  validate-db]
 (fn [db [_ user]]
   (assoc db :user user)))

(re-frame/reg-event-fx
 ::signout
 (fn [_ _]
   {::fx/firabase-signout {:on-success (fn []
                                         (re-frame/dispatch [::set-current-user nil])
                                         (re-frame/dispatch [::navigate :word-penne.pages.auth/signin]))
                           :on-failure (fn [_]
                                         (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(re-frame/reg-event-fx
 ::fetch-cards
 (fn [_ _]
   {::fx/firebase-load-cards {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                              :on-success (fn [cards] [::set-cards cards])}}))

(re-frame/reg-event-db
 ::set-cards
 [(validate-args [:sequential db/t-card])
  validate-db]
 (fn [db [_ res]]
   (assoc db
          :cards res
          :selected-card nil)))

(re-frame/reg-event-fx
 ::create-card
 [(validate-args [:map [:values
                        [:map
                         ["front-text" string?]
                         ["back-text" string?]
                         ["comment" {:optional true} string?]]]])]
 (fn [_ [_ {:keys [values]}]]
   {::fx/firebase-create-card {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                               :values values
                               :on-success (fn [] [::navigate :word-penne.pages.home/home])}}))

(re-frame/reg-event-fx
 ::fetch-card-by-uid
 (fn [{:keys [db]} [_ card-uid]]
   {:db (assoc db :selected-card nil)
    ::fx/firebase-load-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                    :card-uid card-uid
                                    :on-success (fn [card] [::set-selected-card card])}}))

(re-frame/reg-event-db
 ::set-selected-card
 [(validate-args db/t-card)
  validate-db]
 (fn [db [_ res]]
   (assoc db :selected-card res)))

(re-frame/reg-event-fx
 ::update-card-by-uid
 (fn [_ [_ card-uid {:keys [values]}]]
   {::fx/firebase-update-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                      :card-uid card-uid
                                      :values values
                                      :on-success (fn [] [::navigate :word-penne.pages.home/home])}}))
