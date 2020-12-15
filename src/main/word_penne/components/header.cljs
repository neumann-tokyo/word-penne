(ns word-penne.components.header
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.components.button :refer [Button]]))

; style
(def s-topnav
  {:overflow "hidden"
   :border-bottom (str "solid 1px " (:assort-border color))
   :width "100%"
   :height (:header-height layout-vars)
   :padding ".5rem"
   :display "flex"
   :justify-content "space-between"
   :align-items "center"
   :box-sizing "border-box"
   :flex-wrap "wrap"})
(def s-title
  {:margin "0 .5rem"})
(def s-search-container
  {:flex 1})
(def s-search-form
  {:border-radius "10px"
   :background (:assort-background color)
   :display "inline-block"})
(def s-search-button
  {:padding ".5rem"
   :font-size "1.5rem"
   :border "none"
   :cursor "pointer"
   :background "none"
   ::stylefy/mode {:focus {:outline "none"}}})
(def s-search-box
  {:border "none"
   :background "none"
   :padding ".5rem"
   :font-size "1.5rem"
   ::stylefy/mode {:focus {:outline "none"}}})

(defn Header []
  [:header (use-style s-topnav)
   [:a (use-style s-title {:href "/"}) "Word Penne"]
   [:div (use-style s-search-container)
    [:div (use-style s-search-form)
     [:button (use-style s-search-button {:type "submit"})
      [:span {:class "material-icons-outlined"} "search"]]
     [:input (use-style s-search-box {:type "search" :placeholder "Search..." :name "search"})]]]
   [Button {:href "#"} "Sign In"]
   [Button {:href "#"} "Sign Up"]])
