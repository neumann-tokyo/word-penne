(ns word-penne.components.navigation
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def s-navigation
  {:height "100%"
   :width "0" ; TODO この値を動的に変える
   :position "fixed"
   :top "4rem"
   :left 0
   :border-right "1px solid #ccc"
   :background "#F9F5F1"
   :overflow-x "hidden"
   :transition "0.5s"})
(def s-nav-close
  {:padding "1rem"
   :text-align "right"})
(def s-nav-link
  {:padding "1rem"
   :text-decoration "none"
   :font-size "1.5rem"
   :color "#818181"
   :display "block"
   ::stylefy/mode {:hover {:background "#cccccc"}}})
(def s-nav-close-link
  {:text-decoration "none"
   :display "inline-block"
   :font-size "1.5rem"
   :color "#818181"
   :width "2rem"
   :height "2rem"
   ::stylefy/mode {:hover {:background "#cccccc"
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
