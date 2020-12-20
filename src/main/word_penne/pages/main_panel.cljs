(ns word-penne.pages.main-panel
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.style.vars :refer [color phone-width z-indexs]]
            [word-penne.components.header :refer [Header]]
            [word-penne.components.navigation :refer [Navigation]]
            [word-penne.components.word-card-add-button :refer [WordCardAddButton]]
            [word-penne.pages.home]
            [word-penne.pages.cards]
            [word-penne.pages.auth]))

(def s-main-panel
  {:background-color (:main-background color)
   :color (:main-text color)
   :height "100vh"})
(def s-main-container
  {:display "flex"})
(def s-main
  {:width "100%"
   :flex 1})
(def s-word-card-add-button
  {:display "none"
   ::stylefy/media {phone-width {:display "inline-block"
                                 :position "fixed"
                                 :bottom 0
                                 :right 0
                                 :margin "1rem"
                                 :z-index (:bottom-word-card-add-button z-indexs)}}})

(defn main-panel []
  [:div (use-style s-main-panel)
   [Header]
   [:div (use-style s-main-container)
    [Navigation]
    [:main (use-style s-main {:id "main"}) [v/view @(re-frame/subscribe [::subs/current-route])]]]
   [:div (use-style s-word-card-add-button)
    [WordCardAddButton]]])
