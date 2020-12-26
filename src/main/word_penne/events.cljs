(ns word-penne.events
  (:require [re-frame.core :as re-frame]
            [cljs.spec.alpha :as s]
            ;; [bidi.bidi :as bidi]
            [word-penne.db :as db]
            [word-penne.fx :as fx]
            [word-penne.subs :as subs]
            [word-penne.config :as config]
            ;; [word-penne.routes :refer [routes]]
            ))

(s/check-asserts config/debug?)

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when (and (s/check-asserts?) (not (s/valid? a-spec db)))
    (throw (js/Error. (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {})))))

(def check-spec-interceptor (re-frame/after (partial check-and-throw ::db/db)))

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
 [check-spec-interceptor]
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
 [check-spec-interceptor]
 (fn [db [_ res]]
   (assoc db
          :cards res
          :selected-card nil)))

(re-frame/reg-event-fx
 ::create-card
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
 [check-spec-interceptor]
 (fn [db [_ res]]
   (assoc db :selected-card res)))

(re-frame/reg-event-fx
 ::update-card-by-uid
 (fn [_ [_ card-uid {:keys [values]}]]
   {::fx/firebase-update-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                      :card-uid card-uid
                                      :values values
                                      :on-success (fn [] [::navigate :word-penne.pages.home/home])}}))
