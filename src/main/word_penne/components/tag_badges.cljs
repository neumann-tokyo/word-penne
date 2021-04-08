(ns word-penne.components.tag-badges
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color]]))

(def s-tag
  {:border-radius "1rem 1.5rem"
   :background-color (:assort-background color)
   :border (str "1px solid " (:assort-border color))
   :margin "0 .1rem"
   :padding "0 .5rem"})

(defn TagBadges [tags]
  [:div
   (doall (map-indexed
           (fn [index tag]
             [:span (use-style s-tag {:key index}) tag])
           tags))])
