(ns word-penne.components.word-card-add-button
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color]]))

(def s-create-word-card-button
  {:background-color (:accent-background color)
   :border (str "solid 1px " (:accent-border color))
   :color (:accent-text color)
   :width "2.5rem"
   :height "2.5rem"
   :display "inline-block"
   :text-align "center"
   :text-decoration "none"
   :border-radius "50%"
   ::stylefy/mode {:hover {:background-color (:accent-border color)}}})
(def s-create-word-card-button-item
  {:font-size "2.5rem"
   :font-weight "bold"})


(defn WordCardAddButton [_]
  [:a (use-style s-create-word-card-button {:href (path-for routes :word-penne.pages.cards/new)
                                            :title "New"})
   [:span (use-style s-create-word-card-button-item {:class "material-icons-outlined"}) "add"]])
