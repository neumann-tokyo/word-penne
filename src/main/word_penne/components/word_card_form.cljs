(ns word-penne.components.word-card-form
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [fork.reagent :as fork]
            [word-penne.style.vars :refer [color]]))

(def s-form
  {:display "inline-block"
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :box-shadow (str "0 2px 4px 0 " (:assort-border color))
   :padding "1rem"})

(defn WordCardForm [props]
  [fork/form (merge {:prevent-default? true
                     :clean-on-unmount? true}
                    props)
   (fn [{:keys [values
                form-id
                handle-change
                handle-blur
                submitting?
                handle-submit]}]
     [:form (use-style s-form {:id form-id
                               :on-submit handle-submit})
      [:div
       [:label {:for "front"} "Front"]
       [:input {:type "text" :id "front" :name "front"}]]
      [:div
       [:lavel {:for "back"} "Back"]
       [:input {:type "text" :id "back" :name "back"}]]
      [:div
       [:lavel {:for "comment"} "Comment"]
       [:input {:type "text" :id "comment" :name "comment"}]]
      [:div
       [:lavel {:for "tags"} "Tags"]
       [:input {:type "text" :id "tags" :name "tags"}]]
      [:div
       [:input {:type "submit" :value "Submit"}]]])])
