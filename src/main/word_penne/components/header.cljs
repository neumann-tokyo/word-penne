(ns word-penne.components.header
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color layout-vars z-indexs]]
            [word-penne.components.button :refer [Button]]))

(def s-topnav
  {:position "sticky"
   :top "-16px"
   :z-index (:header z-indexs)
   :width "100%"
   :height (str "calc(16px + " (:header-height layout-vars) ")")
   :-webkit-backface-visibility "hidden"
   :backface-visibility "hidden"
   ::stylefy/mode {:before {:content "''"
                            :display "block"
                            :height "16px"
                            :position "sticky"
                            :top (str "calc(" (:header-height layout-vars) " - 16px)")
                            :box-shadow (str "0 2px 4px 0 " (:assort-border color))}
                   :after {:content "''"
                           :display "block"
                           :height "16px"
                           :position "sticky"
                           :background (:main-background color)
                           :top 0
                           :z-index (inc (:header z-indexs))}}})
(def s-topnav-container
  {:border-bottom (str "solid 1px " (:assort-border color))
   :background-color (:main-background color)
   :color (:main-text color)
   :height (:header-height layout-vars)
   :width "100%"
   :padding ".5rem"
   :position "sticky"
   :top "0"
   :margin-top "-16px"
   :z-index 3 ;; TODO 
   :box-sizing "border-box"
   :overflow "hidden"
   :display "flex"
   :justify-content "space-between"
   :align-items "center"
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
   [:div (use-style s-topnav-container)
    [:a (use-style s-title {:href (path-for routes :word-penne.pages.home/home)}) "Word Penne"]
    [:div (use-style s-search-container)
     [:div (use-style s-search-form)
      [:button (use-style s-search-button {:type "submit"})
       [:span {:class "material-icons-outlined"} "search"]]
      [:input (use-style s-search-box {:type "search" :placeholder "Search..." :name "search"})]]]
    [Button {:href "#"} "Sign In"]
    [Button {:href "#"} "Sign Up"]]])
