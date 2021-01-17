(ns word-penne.components.button
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color]]
            [word-penne.style.share :as share]))

(def s-button
  share/m-button)
(def s-button-secondary
  (merge share/m-button
         {:background (:main-background color)
          :color (:accent-border color)
          :border (str "solid 2px " (:accent-border color))
          :font-weight "bold"}))
(def s-button-primary
  (merge share/m-button
         {:margin-right ".5rem"
          :outline "none"
          :background (:accent-background color)
          :border (str "solid 1px " (:accent-border color))
          :color (:accent-text color)
          :cursor "pointer"
          ::stylefy/mode {:hover {:background-color (:accent-border color)}}}))
(defn button-design [kind]
  (case kind
    "primary" s-button-primary
    "secondary" s-button-secondary
    s-button))

(defn Button [attrs text]
  (let [kind (:kind attrs)
        html-attrs (dissoc attrs :kind)]
    [:a (use-style (button-design kind) html-attrs) text]))
