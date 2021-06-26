(ns word-penne.style.form
  (:require [stylefy.core :as stylefy]
            [word-penne.style.share :as share]
            [word-penne.style.vars :refer [color phone-width]]))

(def s-form-container
  {:display "flex"
   :justify-content "center"})
(def s-form
  {:display "inline-block"
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :box-shadow (str "0 2px 4px 0 " (:assort-border color))
   :padding "1rem"
   :width "80%"
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
          :cursor "pointer"
          ::stylefy/mode {:hover {:background-color (:accent-border color)}}}))
(def s-error-message
  {:color (:error-text color)
   :margin-bottom "1rem"})
