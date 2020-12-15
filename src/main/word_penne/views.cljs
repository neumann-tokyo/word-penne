(ns word-penne.views
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.components.header :refer [Header]]
            [word-penne.components.navigation :refer [Navigation]]))

(def s-home
  {:color "red"})

;; TODO I want to splite pages
(defmulti view :handler)

(defmethod view ::home [_]
  [:div (use-style s-home) "Home"
   [:a {:href (path-for routes :word-penne.views/list)} "list"]])

(defmethod view ::list [_]
  [:div "Todo List"
   [:a {:href (path-for routes :word-penne.views/home)} "top"]])

(defmethod view ::create [_]
  [:div "Create New Todo"])

(defmethod view ::edit [{:keys [route-params]}]
  [:div (str "Edit Todo " (:id route-params))])

(defmethod view :default [_]
  [:div "404 Not Found"])

;; ------------------
(def s-main-panel
  {:background-color (:main-background color)
   :color (:main-text color)
   :transition "margin-left .5s"
   :height "100vh"})
(def s-header
  {:position "fixed"
   :top 0
   :width "100%"
   :background-color "inherit"
   :color "inherit"})
(def s-main-container
  {:margin-top (:header-height layout-vars)})
(def s-main
  {:width "100%"
   :margin-left (:navigation-width layout-vars)})

(defn main-panel []
  [:div (use-style s-main-panel)
   [:div (use-style s-header)
    [Header]]
   [:div (use-style s-main-container)
    [Navigation]
    [:main (use-style s-main {:id "main"}) [view @(re-frame/subscribe [::subs/current-route])]]]])
