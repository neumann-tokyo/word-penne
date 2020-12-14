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
   :display "grid"
   :grid-template-areas "'header header header' 'nav content side' 'footer footer footer'"
   :grid-template-columns "200px 1fr 200px"
   :grid-template-rows "auto 1fr auto"
   :grid-gap "10px"})

(def s-header
  {:grid-area "header"})

(def s-nav
  {:grid-area "nav"
   :margine-left "0.5rem"})

(def s-main
  {:grid-area "content"})

(def s-aside
  {:grid-area "side"
   :margin-right "0.5rem"})

(def s-footer
  {:grid-area "footer"})

(defn main-panel []
  [:div (use-style s-main-panel)
   [Header (use-style s-header)]
   [:nav (use-style s-nav)]
   [:main (use-style s-main) [view @(re-frame/subscribe [::subs/current-route])]]
   [:aside (use-style s-aside)]
   [:footer (use-style s-footer)]])
