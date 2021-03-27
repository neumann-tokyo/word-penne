(ns word-penne.components.tags-form
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [fork.reagent :as fork]
            [bidi.bidi :refer [path-for]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]
            [word-penne.style.form :as sf]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.tags-input :refer [TagsInput]]
            [word-penne.i18n :refer [tr]]))

(defn TagsForm [props]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style sf/s-form-container)
   [fork/form (merge {:path [:form]
                      :prevent-default? true
                      :clean-on-unmount? true
                      :initial-values {"tags" [{"name" "" "beforeName" ""}]}}
                     props)
    (fn [{:keys [form-id
                 submitting?
                 handle-submit] :as f-props}]
      [:form (use-style sf/s-form {:id form-id
                                   :on-submit handle-submit})
       [:div
        [:label {:for "tags"} (tr "Tags")]
        [fork/field-array {:props f-props
                           :name "tags"}
         TagsInput]]
       (when-let [error @(re-frame/subscribe [::subs/tags-error])]
         [:div (use-style sf/s-error-message) error])
       [:div (use-style sf/s-buttons-container)
        [:button (use-style sf/s-submit {:type "submit" :data-testid "word-card-form__submit" :disabled submitting?}) (tr "Submit")] ;; TODO FIXME double submit を回避できてない...
        [Button {:href (path-for routes :word-penne.pages.home/home)} (tr "Cancel")]]])]])
