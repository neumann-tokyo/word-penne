(ns word-penne.components.toggle-switch
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color]]))

(def s-switch
  {:position "relative"
   :display "inline-flex"
   :align-items "center"
   :height "34px"})
(def s-checkbox
  {:opacity 0
   :width 0
   :height 0
   ::stylefy/manual [[:&:checked
                      ["+ .slider" {:background-color (:assort-border color)}]
                      ["+ .slider:before" {:transform "translateX(27px) rotate(-10deg)"
                                           :background-color (:main-background color)}]]]})
(def s-slider
  {:position "absolute"
   :cursor "pointer"
   :display "inline-block"
   :top 0
   :left 0
   :right 0
   :bottom 0
   :width "60px"
   :background-color (:main-background color)
   :transition ".4s"
   :border-radius "15px 26px"
   :border (str "solid 2px" (:assort-border color))
   ::stylefy/mode {:before {:position "absolute"
                            :content "''"
                            :height "26px"
                            :width "20px"
                            :left "5px"
                            :bottom "2px"
                            :background-color (:assort-border color)
                            :transition ".4s"
                            :transform "rotate(-10deg)"
                            :border-radius "15px 26px"}}})
(def s-text
  {:margin-left "66px"
   :cursor "pointer"})

(defn ToggleSwitch [attrs text]
  [:label (use-style s-switch)
   [:input
    (use-style s-checkbox {:type "checkbox"
                           :on-change (:on-change attrs)})]
   [:span.slider (use-style s-slider)]
   [:span (use-style s-text) text]])
