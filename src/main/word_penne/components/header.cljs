(ns word-penne.components.header
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def s-topnav
  {:overflow "hidden"
   :border-bottom "solid 1px #e9e9e9"
   :position "fixed"
   :top 0
   :width "100%"
   :height "40px"
   :display "flex"
   :align-items "center"
   :box-sizing "border-box"
   :flex-wrap "wrap"})

(def s-link
  {})

(def s-search-container
  {:flex 1})

(defn Header []
  [:header (use-style s-topnav)
   [:a {:href "/"} "Word Penne"]
   [:div (use-style s-search-container)
    [:input {:type "search" :placeholder "Search..." :name "search"}]
    [:button {:type "submit"}
     [:i {:class "material-icons"} "search"]]]
   [:a {:href "#"} "Sign In"]
   [:a {:href "#"} "Sign Up"]])
