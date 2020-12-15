(ns word-penne.components.word-card
  (:require [stylefy.core :as stylefy :refer [use-style use-sub-style]]
            [word-penne.style.vars :refer [color]]))

(def s-card
  {;  :border (str "solid 1px " (:assort-border color))
  ;  :border-radius ".5rem"
   :display "inline-block"})
(def s-flip-card
  {:background-color "transparent"
   :border "none"
   :perspective "1000px"
   :width "15rem"
   :height "10rem"
   ::stylefy/mode {:focus {:outline "none"}}
   ::stylefy/manual [[:&:focus-within [:.flipcard_inner {:transform "rotateY(180deg)"
                                                         :border "none"}]]]})
(def s-flip-card-inner
  {:position "relative"
   :width "100%"
   :height "100%"
   :transition "transform 0.6s"
   :transform-style "preserve-3d"})

(def m-flip-card
  {:position "absolute"
   :width "100%"
   :height "100%"
   :-webkit-backface-visibility "hidden"
   :backface-visibility "hidden"
   :color (:main-text color)
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :padding-top "1rem"
   ::stylefy/mode {:hover {:box-shadow (str "0 2px 4px 0 " (:assort-border color))}}})
(def s-flip-card-front
  (merge m-flip-card {:background-color (:main-background color)
                      :font-size "2rem"
                      :font-weight "bold"
                      :text-align "center"}))
(def s-flip-card-back
  (merge m-flip-card {:background-color (:assort-background color)
                      :transform "rotateY(180deg)"
                      :display "flex"
                      :flex-direction "column"}))
(def s-flip-card-back-title-container
  {:flex "1"})
(def s-flip-card-back-title
  {:font-size "2rem"
   :font-weight "bold"
   :text-align "center"
   :outline "none"})
(def s-flip-card-buttons
  {:text-align "right"
   :font-size ".8rem"
   :padding-right ".5rem"})
(def s-flip-card-button
  {:color (:main-text color)})

(defn WordCard [params]
  [:div (use-style s-card)
   [:button (use-style s-flip-card)
    [:div.flipcard_inner (use-style s-flip-card-inner)
     [:div (use-style s-flip-card-front) (:front-text params)]
     [:div (use-style s-flip-card-back)
      [:div (use-style s-flip-card-back-title-container)
       (if (nil? (:comment params))
         [:div (use-style s-flip-card-back-title) (:back-text params)]
         [:details
          [:summary (use-style s-flip-card-back-title) (:back-text params)]
          [:p (:comment params)]])]
      [:div (use-style s-flip-card-buttons)
       [:a (use-style s-flip-card-button {:href "#"})
        [:span {:class "material-icons-outlined"} "edit"]]
       [:a (use-style s-flip-card-button {:href "#"})
        [:span {:class "material-icons-outlined"} "delete"]]]]]]])
