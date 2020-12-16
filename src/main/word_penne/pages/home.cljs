(ns word-penne.pages.home
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.views :as v]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]))

(def s-create-word-card-button
  {:background-color (:accent-background color)
   :border (str "solid 1px " (:accent-border color))
   :color "white"
   :width "3rem"
   :height "3rem"
   :display "inline-block"
   :text-align "center"
   :text-decoration "none"
   :border-radius "50%"
   :position "absolute"
   :top (str "calc(" (:header-height layout-vars) " + 1rem)")
   :right "2rem"
   ::stylefy/mode {:hover {:background-color (:accent-border color)}}})
(def s-create-word-card-button-item
  {:font-size "3rem"
   :font-weight "bold"})

(defmethod v/view ::home [_]
  [:div
   [:a (use-style s-create-word-card-button {:href (path-for routes :word-penne.pages.new-card/new-card)})
    [:span (use-style s-create-word-card-button-item {:class "material-icons-outlined"}) "add"]]
   [WordCardsWrap]])
