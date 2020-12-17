(ns word-penne.components.header
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color layout-vars z-indexs phone-width]]
            [word-penne.components.word-card-add-button :refer [WordCardAddButton]]))

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
   :overflow "hidden"
   :display "flex"
   :justify-content "space-between"
   :align-items "center"})
(def s-title
  {:margin "0 .5rem"
   :text-decoration "none"
   :display "flex"
   :flex-direction "row"
   :align-items "center"
   :color (:title-text color)})
(def s-title-logo
  {:width "3rem"
   :height "3rem"})
(def s-title-text
  {:font "bold 2rem Cursive"
   ::stylefy/media {phone-width {:display "none"}}})
(def s-search-container
  {:flex 1})
(def s-search-form
  {:border-radius "10px"
   :background (:assort-background color)
   :display "inline-flex"
   :flex-direction "row"
   :align-items "center"})
(def s-search-button
  {:padding "0 .5rem"
   :font-size "1rem"
   :border "none"
   :cursor "pointer"
   :background "none"
   ::stylefy/mode {:focus {:outline "none"}}})
(def s-search-box
  {:border "none"
   :background "none"
   :padding ".5rem"
   :font-size "1rem"
   ::stylefy/mode {:focus {:outline "none"}}})
(def s-word-card-add-button
  {::stylefy/media {phone-width {:display "none"}}})

;; https://stijndewitt.com/2018/06/12/pure-css-drop-shadow-on-scroll/
(defn Header []
  [:header (use-style s-topnav)
   [:div (use-style s-topnav-container)
    [:a (use-style s-title {:href (path-for routes :word-penne.pages.home/home)})
     [:img (use-style s-title-logo {:src "/images/word-penne-mini.svg" :alt "Word Penne"})]
     [:span (use-style s-title-text) "Word Penne"]]
    [:div (use-style s-search-container)
     [:div (use-style s-search-form)
      [:button (use-style s-search-button {:type "submit"})
       [:span {:class "material-icons-outlined"} "search"]]
      [:input (use-style s-search-box {:type "search" :placeholder "Search..." :name "search"})]]]
    [:div (use-style s-word-card-add-button)
     [WordCardAddButton]]]])
