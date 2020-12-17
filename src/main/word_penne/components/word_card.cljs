(ns word-penne.components.word-card
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color]]))

(def s-card
  {:display "inline-block"
   :box-sizing "border-box"})
(def s-flip-card
  {:background-color "transparent"
   :border "none"
   :perspective "1000px"
   :min-width "10rem"
   :max-width "40rem"
   :height "9rem"
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
  {:box-sizing "border-box"
   :position "relative"
   :width "100%"
   :height "100%"
   :-webkit-backface-visibility "hidden"
   :backface-visibility "hidden"
   :color (:main-text color)
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :word-wrap "break-word"
   :padding ".5rem"
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
                      :flex-direction "column"
                      :top "-9rem"}))
(def s-flip-card-front-title
  {:margin-top ".5rem"})
(def s-flip-card-back-title-container
  {:flex "1"})
(def s-flip-card-back-title
  {:font-size "2rem"
   :font-weight "bold"
   :text-align "center"
   :outline "none"
   :margin-top ".5rem"})
(def s-flip-card-buttons
  {:text-align "right"
   :font-size ".8rem"
   :display "flex"
   :flex-direction "row"
   :justify-content "space-between"})
(def s-flip-card-button
  {:color (:main-text color)})

;; https://www.w3schools.com/howto/howto_css_flip_card.asp
;; https://www.w3schools.com/tags/tag_details.asp
(defn WordCard [params]
  [:div (use-style s-card)
   [:button (use-style s-flip-card)
    [:div.flipcard_inner (use-style s-flip-card-inner)
     [:div (use-style s-flip-card-front)
      [:div (use-style s-flip-card-front-title) (:front-text params)]]
     [:div (use-style s-flip-card-back)
      [:div (use-style s-flip-card-back-title-container)
       (if (nil? (:comment params))
         [:div (use-style s-flip-card-back-title) (:back-text params)]
         [:details
          [:summary (use-style s-flip-card-back-title) (:back-text params)]
          [:p (:comment params)]])]
      [:div (use-style s-flip-card-buttons)
       [:div
        [:a (use-style s-flip-card-button {:href "#" :title "pin"})
         [:span {:class "material-icons-outlined"} "push_pin"]]
        [:a (use-style s-flip-card-button {:href (path-for routes :word-penne.pages.cards/edit :id "aaaaaa") :title "edit"})
         [:span {:class "material-icons-outlined"} "edit"]]]
       [:div
        [:a (use-style s-flip-card-button {:href "#" :title "archive"})
         [:span {:class "material-icons-outlined"} "archive"]]
        [:a (use-style s-flip-card-button {:href "#" :title "delete"})
         [:span {:class "material-icons-outlined"} "delete"]]]]]]]])
