(ns word-penne.components.signouted-header
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color layout-vars z-indexs phone-width]]))

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
                           :z-index (:header-shadow z-indexs)}}})
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
   :z-index (:header-shadow-blind z-indexs)
   :box-sizing "border-box"
   :display "flex"
   :justify-content "flex-start"
   :align-items "center"})
(def s-title
  {:margin "0 .5rem"
   :text-decoration "none"
   :display "inline-flex"
   :flex-direction "row"
   :align-items "center"
   :color (:title-text color)})
(def s-title-logo
  {:width "3rem"
   :height "3rem"})
(def s-title-text
  {:font "bold 2rem Cursive"
   ::stylefy/media {phone-width {:display "none"}}})

(defn SignoutedHeader []
  [:header (use-style s-topnav)
   [:div (use-style s-topnav-container)
    [:span (use-style s-title)
     [:img (use-style s-title-logo {:src "/images/word-penne-mini.svg" :alt "Word Penne"})]
     [:span (use-style s-title-text) "Word Penne"]]]])
