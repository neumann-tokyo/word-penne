(ns word-penne.pages.user
  (:require [stylefy.core :as stylefy :refer [use-style]]
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
   [:locale db/t-locale]])

(defmethod v/view ::edit [_]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style sf/s-form-container)
   [fork/form {:path [:form]
               :prevent-default? true
               :clean-on-unmount? true
               :validation (va/validator-for-humans t-user-setting-form)
               :initial-values {"locale" (or @(re-frame/subscribe [::subs/locale]) "en")}
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
        [:select (use-style sf/s-text {:value (values "locale") :name "locale" :id "locale" :on-change handle-change :on-blur handle-blur :required true :data-testid "user-setting__locale"})
         [:option {:value "en"} "English"]
         [:option {:value "ja"} "日本語"]]
        [ErrorMessange touched errors "locale"]]
       [:div (use-style sf/s-buttons-container)
        [:button (use-style sf/s-submit {:type "submit" :data-testid "user-setting__submit" :disabled submitting?}) (tr "Submit")] ;; FIXME double submit
        [Button {:href (path-for routes :word-penne.pages.home/home)} (tr "Cancel")]]])]])
