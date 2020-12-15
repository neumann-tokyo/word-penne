(ns word-penne.components.word-card
  (:require [stylefy.core :as stylefy :refer [use-style use-sub-style]]
            [word-penne.style.vars :refer [color]]))

(def s-card
  {;  :border (str "solid 1px " (:assort-border color))
  ;  :border-radius ".5rem"
   :display "inline-block"})
; :box-shadow "0 4px 8px 0 rgba(0,0,0,0.2)"
(def s-flip-card
  {:background-color "transparent"
   :border "none"
   :perspective "1000px"
   :width "300px"
   :height "300px"
   ::stylefy/mode {:focus {:outline "none"}}
   ::stylefy/manual [[:&:focus [:.flipcard_inner {:transform "rotateY(180deg)"
                                                  :border "none"}]]]})
(def s-flip-card-inner
  {:position "absolute"
   :width "100%"
   :height "100%"
   :transition "transform 0.6s"
   :transform-style "preserve-3d"})
(def s-flip-card-front
  {:position "absolute"
   :width "100%"
   :height "100%"
   :-webkit-backface-visibility "hidden"
   :backface-visibility "hidden"
   :background-color "blue"
   :font-size "2rem"
   :font-weight "bold"
   :text-align "center"})
(def s-flip-card-back
  {:width "100%"
   :height "100%"
   :-webkit-backface-visibility "hidden"
   :backface-visibility "hidden"
   :background-color "yellow"
   :font-size "2rem"
   :font-weight "bold"
   :text-align "center"
   :transform "rotateY(180deg)"})

(defn WordCard [params]
  [:div (use-style s-card)
   [:button (use-style s-flip-card)
    [:div.flipcard_inner (use-style s-flip-card-inner)
     [:div (use-style s-flip-card-front) (:front-text params)]
     [:div (use-style s-flip-card-back) (:back-text params)]]]
   [:div "panel"]])
