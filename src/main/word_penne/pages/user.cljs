(ns word-penne.pages.user
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [fork.reagent :as fork]
            [word-penne.views :as v]
            [word-penne.routes :refer [routes]]
            [word-penne.style.form :as sf]
            [word-penne.components.button :refer [Button]]))

(defmethod v/view ::edit [_]
  [:div (use-style sf/s-form-container)
   [fork/form {:path [:form]
               :prevent-default? true
               :clean-on-unmount? true
                      ;:validation (v/validator-for-humans t-card-form)
               }
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
        [:label {:for "locale"} "Locale"]
        [:select (use-style sf/s-text {:name "locale" :id "locale" :on-change handle-change :on-blur handle-blur :required true :data-testid "user-setting__locale"})
         [:option {:value "en"} "English"]
         [:option {:value "ja"} "日本語"]]
        [:div (use-style sf/s-buttons-container)
         [:button (use-style sf/s-submit {:type "submit" :data-testid "user-setting__submit" :disabled submitting?}) "Submit"]
         [Button {:href (path-for routes :word-penne.pages.home/home)} "Cancel"]]]])]])
