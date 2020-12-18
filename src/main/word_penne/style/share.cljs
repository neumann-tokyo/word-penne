(ns word-penne.style.share
  (:require [stylefy.core :as stylefy]
            [word-penne.style.vars :refer [color]]))

(def m-button
  {:background (:assort-background color)
   :color (:main-text color)
   :border (str "solid 1px " (:assort-border color))
   :padding ".5rem 1rem"
   :text-align "center"
   :text-decoration "none"
   :display "inline-block"
   :border-radius "10px"
   ::stylefy/mode {:hover {:filter "brightness(90%)"}}})

(def m-card
  {:box-sizing "border-box"
   :background (:main-background color)
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :word-wrap "break-word"
   :color (:main-text color)
   :padding ".5rem"})
