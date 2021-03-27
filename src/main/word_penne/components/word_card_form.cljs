(ns word-penne.components.word-card-form
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [fork.reagent :as fork]
            [bidi.bidi :refer [path-for]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]
            [word-penne.style.form :as sf]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.tags-input :refer [TagsInput]]
            [word-penne.components.error-message :refer [ErrorMessange]]
            [word-penne.validator :as v]
            [word-penne.i18n :refer [tr]]))

(def ^:private t-card-form
  [:map
   [:front [:string {:min 1 :max 140}]]
   [:back [:string {:min 1 :max 140}]]
   [:comment {:optional true} [:maybe [:string {:min 0 :max 140}]]]])

(defn WordCardForm [props]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style sf/s-form-container)
   [fork/form (merge {:path [:form]
                      :prevent-default? true
                      :clean-on-unmount? true
                      :validation (v/validator-for-humans t-card-form)
                      :initial-values {"tags" [{"name" "" "beforeName" ""}]}}
                     props)
    (fn [{:keys [values
                 errors
                 touched
                 form-id
                 handle-change
                 handle-blur
                 submitting?
                 handle-submit] :as f-props}]
      [:form (use-style sf/s-form {:id form-id
                                   :on-submit handle-submit})
       [:div
        [:label {:for "front"} (tr "Front")]
        [:input (use-style sf/s-text {:type "text" :id "front" :name "front" :data-testid "word-card-form__front" :required true :value (values "front") :on-change handle-change :on-blur handle-blur})]
        [ErrorMessange touched errors "front"]]
       [:div
        [:label {:for "back"} (tr "Back")]
        [:input (use-style sf/s-text {:type "text" :id "back" :name "back" :data-testid "word-card-form__back" :required true :value (values "back") :on-change handle-change :on-blur handle-blur})]
        [ErrorMessange touched errors "back"]]
       [:div
        [:label {:for "comment"} (tr "Comment")]
        [:input (use-style sf/s-text {:type "text" :id "comment" :name "comment" :data-testid "word-card-form__comment" :value (values "comment") :on-change handle-change :on-blur handle-blur})]
        [ErrorMessange touched errors "comment"]]
       ;; TODO validate tags
       [:div
        [:label {:for "tags"} (tr "Tags")]
        [fork/field-array {:props f-props
                           :name "tags"}
         TagsInput]
        [:datalist {:id "tag-list"}
         (doall (map-indexed
                 (fn [index tag]
                   [:option {:value tag :key index}])
                 @(re-frame/subscribe [::subs/tags])))]]
       [:div (use-style sf/s-buttons-container)
        [:button (use-style sf/s-submit {:type "submit" :data-testid "word-card-form__submit" :disabled submitting?}) (tr "Submit")] ;; TODO FIXME double submit を回避できてない...
        [Button {:href (path-for routes :word-penne.pages.home/home)} (tr "Cancel")]]])]])
