(ns word-penne.pages.user
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [fork.reagent :as fork]
            [re-frame.core :as re-frame]
            [word-penne.db :as db]
            [word-penne.views :as v]
            [word-penne.validator :as va]
            [word-penne.subs :as subs]
            [word-penne.events :as events]
            [word-penne.routes :refer [routes]]
            [word-penne.style.form :as sf]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.error-message :refer [ErrorMessange]]
            [word-penne.i18n :refer [tr]]))

(def ^:private t-user-setting-form
  [:map
   [:locale db/t-locale]
   [:front-speak-language db/t-speak-language]
   [:back-speak-language db/t-speak-language]])

(defmethod v/view ::edit [_]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style sf/s-form-container)
   [fork/form {:path [:form]
               :prevent-default? true
               :clean-on-unmount? true
               :validation (va/validator-for-humans t-user-setting-form)
               :initial-values {"locale" @(re-frame/subscribe [::subs/locale])
                                "front-speak-language" @(re-frame/subscribe [::subs/front-speak-language])
                                "back-speak-language" @(re-frame/subscribe [::subs/back-speak-language])}
               :on-submit #(re-frame/dispatch [::events/update-user-setting %])}
    (fn [{:keys [values
                 errors
                 touched
                 form-id
                 handle-change
                 handle-blur
                 submitting?
                 handle-submit]}]
      [:form (use-style sf/s-form {:id form-id
                                   :on-submit handle-submit})
       [:div
        [:label {:for "locale"} (tr "Locale")]
        [:select (use-style sf/s-text {:value (values "locale")
                                       :name "locale"
                                       :id "locale"
                                       :on-change handle-change
                                       :on-blur handle-blur
                                       :required true
                                       :data-testid "user-setting__locale"})
         [:option {:value "en"} "English"]
         [:option {:value "ja"} "Japanese"]]
        [ErrorMessange touched errors "locale"]]
       [:div
        [:label {:for "front-speak-language"} (tr "Front speak language")]
        [:select (use-style sf/s-text {:value (values "front-speak-language")
                                       :name "front-speak-language"
                                       :id "front-speak-language"
                                       :on-change handle-change
                                       :on-blur handle-blur
                                       :required true
                                       :data-testid "user-setting__front-speak-language"})
         (doall (map (fn [[code lang]] [:option {:value code :key code} lang]) db/t-speak-language-map))]
        [ErrorMessange touched errors "front-speak-language"]]
       [:div
        [:label {:for "back-speak-language"} (tr "Back speak language")]
        [:select (use-style sf/s-text {:value (values "back-speak-language")
                                       :name "back-speak-language"
                                       :id "back-speak-language"
                                       :on-change handle-change
                                       :on-blur handle-blur
                                       :required true
                                       :data-testid "user-setting__back-speak-language"})
         (doall (map (fn [[code lang]] [:option {:value code :key code} lang]) db/t-speak-language-map))]
        [ErrorMessange touched errors "back-speak-language"]]
       [:div (use-style sf/s-buttons-container)
        [:button (use-style sf/s-submit {:type "submit" :data-testid "user-setting__submit" :disabled submitting?}) (tr "Submit")] ;; FIXME double submit
        [Button {:href (path-for routes :word-penne.pages.home/home)} (tr "Cancel")]]])]])

(def ^:private t-quiz-setting-form
  [:map
   [:locale db/t-locale]
   [:front-speak-language db/t-speak-language]
   [:back-speak-language db/t-speak-language]])

(def ^:private s-checkbox
  {:margin-right ".2rem"})

(defmethod v/view ::quiz-settings [_]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style sf/s-form-container)
   [fork/form {:path [:form]
               :prevent-default? true
               :clean-on-unmount? true
               :validation (va/validator-for-humans t-quiz-setting-form)
               :initial-values (let [quiz-settings @(re-frame/subscribe [::subs/quiz-settings])]
                                 {"tags" (:tags quiz-settings)
                                  "kind" (:kind quiz-settings)
                                  "face" (:face quiz-settings)
                                  "count" (:count quiz-settings)})
               :on-submit (fn [a] (prn "aaa"))
               #_(re-frame/dispatch [::events/update-quiz-setting (walk/keywordize-keys %)])}
    (fn [{:keys [values
                 errors
                 touched
                 form-id
                 handle-change
                 handle-blur
                 submitting?
                 handle-submit]}]
      [:form (use-style sf/s-form {:id form-id
                                   :on-submit handle-submit})
       [:div
        [:label {:for "tags"} (tr "Tags")]
        [:select (use-style sf/s-text {:value (values "tags")
                                       :name "tags"
                                       :id "tags"
                                       :on-change handle-change
                                       :on-blur handle-blur
                                       :data-testid "quiz-setting__tags"})
         [:option {:value ""} "なし"]
         (doall (map (fn [tag] [:option {:value tag :key tag} tag]) @(re-frame/subscribe [::subs/tags])))]
        [ErrorMessange touched errors "tags"]]
       [:div
        [:label (tr "Kind")]
        [:div (use-style sf/s-horizontal-list)
         (doall (map (fn [kind]
                       (let [kind-id (str "kind-" (str/replace kind #"\W" "-"))
                             checked (boolean ((set (values "kind")) kind))]
                         [:span {:key kind-id}
                          [:input (use-style s-checkbox
                                             {:type "checkbox"
                                              :id kind-id
                                              :name kind-id
                                              :value kind
                                              :defaultChecked checked})]
                          [:label {:for kind-id}
                           (tr kind)]]))
                     db/t-quiz-setting-kind))]
        [ErrorMessange touched errors "kind"]]
       [:div
        [:label {:for "face"} (tr "Face")]
        [:select (use-style sf/s-text {:value (values "face")
                                       :name "face"
                                       :id "face"
                                       :on-change handle-change
                                       :on-blur handle-blur
                                       :required true
                                       :data-testid "quiz-setting__face"})
         (doall (map (fn [face] [:option {:value face :key face} (tr face)]) db/t-quiz-setting-face))]
        [ErrorMessange touched errors "face"]]
       [:div
        [:label {:for "count"} (tr "Count")]
        [:input (use-style sf/s-text {:type "text"
                                      :id "count"
                                      :name "count"
                                      :value (values "count")
                                      :on-change handle-change
                                      :on-blur handle-blur})]]
       [:div (use-style sf/s-buttons-container)
        [:button (use-style sf/s-submit {:type "submit" :data-testid "quiz-setting__submit" :disabled submitting?}) (tr "Submit")] ;; FIXME double submit
        [Button {:href (path-for routes :word-penne.pages.home/home)} (tr "Cancel")]]])]])
