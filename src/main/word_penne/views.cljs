(ns word-penne.views
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]
            [word-penne.components.header :refer [Header]]))

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

;; TODO responsive
(def s-main-panel
  {:background-color "#F9F5F1"
   :min-height "100vh"
   :display "flex"
   :flex-direction "column"})

(def s-header
  {:position "fixed"
   :top 0})

(def layout-body
  {:display "flex"
   :flex 1})

(def s-nav
  {:flex "0 0 12rem"
   :order -1})

(def s-main
  {:width "100%"})

(def s-footer
  {})

(defn main-panel []
  [:div (use-style s-main-panel)
   [Header (use-style s-header)]
   [:div (use-style layout-body)
    [:nav (use-style s-nav) "nav"]
    [:main (use-style s-main) [view @(re-frame/subscribe [::subs/current-route])]]]
   [:footer (use-style s-footer) "footer"]])
