(ns word-penne.components.word-card-form
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [fork.reagent :as fork]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.style.share :as share]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.components.button :refer [Button]]))

(def s-form-container
  {:display "flex"
   :justify-content "center"})
(def s-form
  {:display "inline-block"
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :box-shadow (str "0 2px 4px 0 " (:assort-border color))
   :padding "1rem"
   ::stylefy/media {phone-width {:margin "0 .5rem"}}})
(def s-text
  {:width "100%"
   :padding ".5rem"
   :margin ".5rem 0"
   :box-sizing "border-box"
   :outline "none"
   :background (:main-background color)
   :border-top "none"
   :border-left "none"
   :border-right "none"
   :border-bottom (str "solid 1px " (:assort-border color))
   ::stylefy/mode {:hover {:border-bottom (str "solid 1px " (:accent-border color))}}})
(def s-buttons-container
  {:text-align "right"})
(def s-submit
  (merge share/m-button
         {:margin-right ".5rem"
          :outline "none"
          :background (:accent-background color)
          :border (str "solid 1px " (:accent-border color))
          :color (:accent-text color)
          ::stylefy/mode {:hover {:background-color (:accent-border color)}}}))

(defn WordCardForm [props]
  [:div (use-style s-form-container)
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
        [:input (use-style s-text {:type "text" :id "front" :name "front"})]]
       [:div
        [:lavel {:for "back"} "Back"]
        [:input (use-style s-text {:type "text" :id "back" :name "back"})]]
       [:div
        [:lavel {:for "comment"} "Comment"]
        [:input (use-style s-text {:type "text" :id "comment" :name "comment"})]]
       [:div
        [:lavel {:for "tags"} "Tags"]
        [:input (use-style s-text {:type "text" :id "tags" :name "tags"})]]
       [:div (use-style s-buttons-container)
        [:input (use-style s-submit {:type "submit" :value "Submit"})]
        [Button {:href (path-for routes :word-penne.pages.home/home)} "Cancel"]]])]])
