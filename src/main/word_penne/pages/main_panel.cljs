(ns word-penne.pages.main-panel
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.components.header :refer [Header]]
            [word-penne.components.navigation :refer [Navigation]]
            [word-penne.pages.home]))

(def s-main-panel
  {:background-color (:main-background color)
   :color (:main-text color)
   :height "100vh"})
(def s-main-container
  {:margin-top (:header-height layout-vars)
   :display "flex"
   :height "100vh"})
(def s-main
  {:width "100%"
   :height "100vh"
   :transition "margin-left .5s"
   :flex 1})

(defn main-panel []
  [:div (use-style s-main-panel)
   [Header]
   [:div (use-style s-main-container)
    [Navigation]
    [:main (use-style s-main {:id "main"}) [v/view @(re-frame/subscribe [::subs/current-route])]]]])
