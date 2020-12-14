(ns word-penne.views
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :refer [path-for]]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]))

(defmulti view :handler)

(defmethod view ::home [_]
  [:div "Home"
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

(defn main-panel []
  [:div "Todo App"
   [view @(re-frame/subscribe [::subs/current-route])]])
