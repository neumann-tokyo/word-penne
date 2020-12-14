(ns word-penne.components.header
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def s-topnav
  {:overflow "hidden"
   :border-bottom "solid 1px #e9e9e9"
   :position "fixed"
   :top 0
   :width "100%"
   :display "flex"
   :align-items "center"
   :box-sizing "border-box"})

(def s-link
  {})

(def s-search-container
  {})

(defn Header []
  [:header (use-style s-topnav)
   [:a {:href "/"} "Word Penne"]
   [:div (use-style s-search-container)
    [:input {:type "search" :placeholder "Search..." :name "search"}]
    [:button {:type "submit"}
     [:i {:class "material-icons"} "search"]]]
   [:a {:href "#"} "Sign In"]
   [:a {:href "#"} "Sign Up"]])
