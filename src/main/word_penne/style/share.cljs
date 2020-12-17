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
   ::stylefy/mode {:hover {:background-color (:assort-border color)}}})
