(ns word-penne.components.navigation
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.subs :as subs]
            [word-penne.events :as events]))

(def s-navigation
  {:background (:main-background color)})
(def s-navigation-checkbox
  {:display "none"
   ::stylefy/manual [[:&:checked ["~" [:#navigation-menu {:width (:navigation-width layout-vars)
                                                          :opacity 1
                                                          :visibility "visible"}]]]]})
(def s-opened-navigation
  {:width (:closed-navigation-width layout-vars)
   :opacity 0
   :visibility "hidden"
   :transition "0.5s cubic-bezier(0.77,0.2,0.05,1.0)"
   :background-color (:main-background color)
   :color (:main-text color)})
(def s-nav-close
  {:margin-right ".5rem"
   :text-align "right"})
(def s-nav-link
  {:padding "1rem"
   :text-decoration "none"
   :font-size "1.5rem"
   :color (:main-text color)
   :display "block"
   ::stylefy/mode {:hover {:background (:assort-background color)}}})
(def s-nav-close-link
  {:text-decoration "none"
   :display "inline-block"
   :font-size "1.5rem"
   :color (:main-text color)
   :width "2rem"
   :height "2rem"
   ::stylefy/mode {:hover {:background (:assort-background color)
                           :border-radius "1rem"}}})
(def s-nav-open
  {:margin-left ".5rem"})
(def s-nav-open-link
  {:text-decoration "none"
   :display "inline-block"
   :font-size "1.5rem"
   :color (:main-text color)
   :width "2rem"
   :height "2rem"
   ::stylefy/mode {:hover {:background (:assort-background color)
                           :border-radius "1rem"}}})

(def s-nav-open-icon
  {:margin-top ".3rem"
   :margin-left ".3rem"})

;; Pure CSS Hamburger menu
;; https://codepen.io/erikterwan/pen/EVzeRP
(defn Navigation []
  [:nav (use-style s-navigation)
   [:input (use-style s-navigation-checkbox {:type "checkbox" :id "navigation-menu-checkbox" :name "navigation-menu-checkbox"})]
   [:label {:for "navigation-menu-checkbox"}
    [:span#navigation-menu-open-button (use-style s-nav-open-icon {:class "material-icons-outlined"}) "menu"]]
   [:div#navigation-menu (use-style s-opened-navigation)
    [:div (use-style s-nav-close)]
    [:a (use-style s-nav-link {:href "#"}) "tag1"]
    [:a (use-style s-nav-link {:href "#"}) "tag2"]
    [:a (use-style s-nav-link {:href "#"}) "tag3"]]])
