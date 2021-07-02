(ns word-penne.events
  (:require [re-frame.core :as re-frame]
            [malli.core :as m]
            [cljs.pprint :refer [pprint]]
            [clojure.string :as str]
            [word-penne.db :as db]
            [word-penne.fx :as fx]
            [word-penne.subs :as subs]
            [word-penne.config :as config]))

(def check-asserts? config/debug?)

(defn- check-and-throw
  "Throws an exception if `form` doesn't match the Spec `a-spec`."
  [validation-type event-name a-spec form]
  (when (and check-asserts? (not (m/validate a-spec form)))
    (throw (js/Error. (str "spec check failed: \n"
                           "method: " validation-type "\n"
                           "event: " event-name "\n"
                           (with-out-str (pprint (m/explain a-spec form))))))))
(def ^:private validate-db
  (re-frame/->interceptor
   :id :validate-db
   :after (fn [{{:keys [event]} :coeffects
                {:keys [db]} :effects :as context}]
            (check-and-throw :validate-db (first event) db/t-db db)
            context)))
(defn- validate-args
  ([a-spec] (validate-args 0 a-spec))
  ([index a-spec]
   (re-frame/->interceptor
    :id :validate-args
    :before (fn [{{:keys [event]} :coeffects :as context}]
              (check-and-throw :validate-args (first event) a-spec (nth (next event) index))
              context))))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(defmulti on-navigate (fn [view _] view))
(defmethod on-navigate :word-penne.pages.home/home [_ _]
  (re-frame/dispatch [::reset-quiz-pointer])
  (re-frame/dispatch [::reset-tags-error])
  (re-frame/dispatch [::fetch-tags])
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
 [(validate-args [:maybe db/t-user])
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
                              :search-target @(re-frame/subscribe [::subs/search-target])
                              :search-word @(re-frame/subscribe [::subs/search-word])
                              :search-tag @(re-frame/subscribe [::subs/search-tag])
                              :search-archive @(re-frame/subscribe [::subs/search-archive])
                              :cards-order @(re-frame/subscribe [::subs/cards-order])
                              :on-success (fn [cards] (re-frame/dispatch [::set-cards cards]))}}))

(re-frame/reg-event-db
 ::set-cards
 [(validate-args [:sequential db/t-card])
  validate-db]
 (fn [db [_ res]]
   (assoc db
          :cards res
          :selected-card nil)))

(re-frame/reg-event-db
 ::set-clicked-card-uid
 [(validate-args [:maybe string?])
  validate-db]
 (fn [db [_ res]]
   (assoc db
          :clicked-card-uid res)))

(re-frame/reg-event-fx
 ::fetch-tags
 (fn [_ _]
   {::fx/firebase-load-tags {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                             :on-success (fn [tags] (re-frame/dispatch [::set-tags tags]))}}))

(re-frame/reg-event-fx
 ::fetch-user-setting
 (fn [_ _]
   {::fx/firebase-load-user-setting {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                     :on-success (fn [setting] (re-frame/dispatch [::set-locale (:locale setting)]))}}))

(re-frame/reg-event-db
 ::set-tags
 [(validate-args [:sequential string?])
  validate-db]
 (fn [db [_ res]]
   (assoc db :tags res)))

(def t-create-card-arg
  [:map [:values
         [:map
          ["front" string?]
          ["back" string?]
          ["comment" {:optional true} string?]
          ["tags" [:sequential [:map
                                ["name" string?]
                                ["beforeName" string?]]]]]]])
(re-frame/reg-event-fx
 ::create-card
 [(validate-args t-create-card-arg)]
 (fn [_ [_ {:keys [values]}]]
   {::fx/firebase-create-card {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                               :values values
                               :tags @(re-frame/subscribe [::subs/tags])
                               :on-success (fn [] (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(re-frame/reg-event-fx
 ::fetch-card-by-uid
 [(validate-args string?)]
 (fn [{:keys [db]} [_ card-uid]]
   {:db (assoc db :selected-card nil)
    ::fx/firebase-load-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                    :card-uid card-uid
                                    :on-success (fn [card] (re-frame/dispatch [::set-selected-card card]))}}))

(re-frame/reg-event-db
 ::set-selected-card
 [(validate-args db/t-card)
  validate-db]
 (fn [db [_ res]]
   (assoc db :selected-card res)))

(re-frame/reg-event-db
 ::delete-selected-card
 [validate-db]
 (fn [db [_ _]]
   (assoc db :selected-card nil)))

(def t-update-card-by-uid-arg
  [:map [:values
         [:map
          ["uid" string?]
          ["front" string?]
          ["back" string?]
          ["comment" {:optional true} [:maybe string?]]
          ["tags" [:sequential [:map
                                ["name" string?]]]]]]])
(re-frame/reg-event-fx
 ::update-card-by-uid
 [(validate-args 0 string?)
  (validate-args 1 t-update-card-by-uid-arg)]
 (fn [_ [_ card-uid {:keys [values]}]]
   {::fx/firebase-update-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                      :card-uid card-uid
                                      :values values
                                      :tags @(re-frame/subscribe [::subs/tags])
                                      :on-success (fn [] (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(re-frame/reg-event-fx
 ::archive-card
 [(validate-args 0 string?)
  (validate-args 1 boolean?)]
 (fn [_ [_ card-uid archive]]
   {::fx/firebase-archive-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                       :card-uid card-uid
                                       :archive archive
                                       :on-success (fn [] (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(re-frame/reg-event-fx
 ::lock-card
 [(validate-args 0 string?)
  (validate-args 1 boolean?)]
 (fn [_ [_ card-uid lock]]
   {::fx/firebase-lock-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                    :card-uid card-uid
                                    :lock lock
                                    :on-success (fn [] (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(def t-update-tags
  [:map [:values
         [:map
          ["tags"
           [:sequential
            [:map
             ["name" string?]
             ["beforeName" string?]]]]]]])
(re-frame/reg-event-fx
 ::update-tags
 [(validate-args t-update-tags)]
 (fn [_ [_ {:keys [values]}]]
   {::fx/firebase-update-tags {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                               :tags @(re-frame/subscribe [::subs/tags])
                               :values values
                               :on-success (fn [] (re-frame/dispatch [::navigate :word-penne.pages.home/home]))
                               :on-failure (fn [error] (re-frame/dispatch [::set-tags-error error]))}}))

(re-frame/reg-event-db
 ::show-confirmation-modal
 [validate-db]
 (fn [db [_ _]]
   (assoc db :show-confirmation-modal true)))

(re-frame/reg-event-db
 ::hide-confirmation-modal
 [validate-db]
 (fn [db [_ _]]
   (assoc db :show-confirmation-modal false)))

(re-frame/reg-event-fx
 ::delete-card-by-uid
 [(validate-args string?)]
 (fn [_ [_ card-uid]]
   {::fx/firebase-delete-card-by-uid {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                      :card-uid card-uid
                                      :on-success (fn [_]
                                                    (re-frame/dispatch [::hide-confirmation-modal])
                                                    (re-frame/dispatch [::fetch-cards]))}}))

(re-frame/reg-event-fx
 ::set-search-target
 [(validate-args db/t-search-target)
  validate-db]
 (fn [{:keys [db]} [_ search-target]]
   {:db (assoc db :search-target search-target)
    :dispatch [::fetch-cards]}))

(re-frame/reg-event-fx
 ::set-search-word
 [(validate-args db/t-search-word)
  validate-db]
 (fn [{:keys [db]} [_ search-word]]
   {:db (assoc db :search-word search-word)
    :dispatch [::fetch-cards]}))

(re-frame/reg-event-fx
 ::set-search-tag
 [(validate-args [:maybe :string])
  validate-db]
 (fn [{:keys [db]} [_ search-tag]]
   {:db (assoc db :search-tag search-tag)
    :dispatch [::fetch-cards]}))

(re-frame/reg-event-db
 ::reset-quiz-pointer
 [validate-db]
 (fn [db [_ _]]
   (assoc db :quiz-pointer 0)))

(re-frame/reg-event-db
 ::reset-tags-error
 [validate-db]
 (fn [db [_ _]]
   (assoc db :tags-error nil)))

(re-frame/reg-event-db
 ::set-tags-error
 [(validate-args [:maybe :string])
  validate-db]
 (fn [db [_ error]]
   (assoc db :tags-error error)))

(re-frame/reg-event-fx
 ::set-search-archive
 [(validate-args [boolean?])
  validate-db]
 (fn [{:keys [db]} [_ search-archive]]
   {:db (assoc db :search-archive search-archive)
    :dispatch [::fetch-cards]}))

(re-frame/reg-event-fx
 ::set-locale
 [(validate-args db/t-locale)]
 (fn [{:keys [db]} [_ locale]]
   {::fx/i18n-set-locale {:locale locale}
    :db (assoc db :locale locale)}))

(def t-update-user-setting-arg
  [:map [:values
         [:map
          ["locale" db/t-locale]]]])
(re-frame/reg-event-fx
 ::update-user-setting
 [(validate-args t-update-user-setting-arg)]
 (fn [_ [_ {:keys [values]}]]
   {::fx/firebase-update-user-setting {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                       :values values
                                       :on-success (fn []
                                                     (re-frame/dispatch [::set-locale (values "locale")])
                                                     (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(re-frame/reg-event-db
 ::set-quiz-cards0
;;  [(validate-args [:maybe :string])
;;   validate-db]
 (fn [db [_ cards]]
   (assoc db :quiz-cards0 cards)))

(re-frame/reg-event-db
 ::set-reverse-cards
 [(validate-args boolean?)
  validate-db]
 (fn [db [_ res]]
   (assoc db :reverse-cards res)))

(re-frame/reg-event-fx
 ::set-cards-order
 [(validate-args db/t-cards-order)
  validate-db]
 (fn [{:keys [db]} [_ res]]
   {:db (assoc db :cards-order res)
    :dispatch [::fetch-cards]}))

(re-frame/reg-event-fx
 ::setup-quiz0
 (fn [_ _]
   {::fx/firebase-setup-quiz0 {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                               :on-success (fn [cards]
                                             (re-frame/dispatch [::set-quiz-cards0 cards])
                                             (re-frame/dispatch [::navigate :word-penne.pages.cards/quiz0]))}}))

(re-frame/reg-event-db
 ::set-quiz-cards
;;  TODO spec
;;  [(validate-args [:sequential db/t-quiz-card])
;;   validate-db]
 (fn [db [_ cards]]
   (assoc db :quiz-cards cards)))

(re-frame/reg-event-fx
 ::setup-quiz
 (fn [_ _]
   {::fx/firebase-setup-quiz {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                              :on-success (fn [cards]
                                            (re-frame/dispatch [::set-quiz-cards cards])
                                            (re-frame/dispatch [::navigate :word-penne.pages.cards/quiz]))}}))

(re-frame/reg-event-fx
 ::answer-quiz0
 (fn [_ [_ {:keys [values]}]]
   {::fx/firebase-answer-quiz0 {:user-uid (:uid @(re-frame/subscribe [::subs/current-user]))
                                :values values
                                :on-success (fn [] (re-frame/dispatch [::navigate :word-penne.pages.home/home]))}}))

(defn- check-answer [answer correct-text]
  (cond
    (str/blank? answer) false

    (= (str/lower-case (str/trim answer))
       (str/lower-case correct-text)) true

    :else false))

(re-frame/reg-event-db
 ::answer-quiz
 (fn [db [_ {:keys [values]}]]
   (let [judgement (if (check-answer (values "answer") (values "correct-text")) "Correct" "Wrong")
         index @(re-frame/subscribe [::subs/quiz-pointer])]
     (assoc-in db [:quiz-cards index :judgement] judgement))))

(re-frame/reg-event-db
 ::increment-quiz-pointer
 (fn [db [_ _]]
   ;; TODO quiz-pointer が (count quiz-cards) より大きくなったときの処理が必要
   (update db :quiz-pointer inc)))
