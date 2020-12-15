(ns word-penne.components.navigation
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color layout-vars]]))

(def s-navigation
  {:height "100%"
   :width (:navigation-width layout-vars) ; TODO この値を動的に変える
   :position "fixed"
   :top (:header-heihgt layout-vars)
   :left 0
   :border-right (str "1px solid " (:assort-border color))
   :background (:main-background color)
   :overflow-x "hidden"
   :transition "0.5s"})
(def s-nav-close
  {:padding "1rem"
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
(def s-nav-close-icon
  {:margin-top ".2rem"
   :margin-right ".3rem"})

; https://www.w3schools.com/howto/howto_js_off-canvas.asp
; TODO sidnav の開閉
(defn Navigation []
  [:nav (use-style s-navigation {:id "sidenav"})
   [:div (use-style s-nav-close)
    [:a (use-style s-nav-close-link {:href "#"})
     [:span (use-style s-nav-close-icon {:class "material-icons"}) "chevron_left"]]]
   [:a (use-style s-nav-link {:href "#"}) "tag1"]
   [:a (use-style s-nav-link {:href "#"}) "tag2"]
   [:a (use-style s-nav-link {:href "#"}) "tag3"]])
