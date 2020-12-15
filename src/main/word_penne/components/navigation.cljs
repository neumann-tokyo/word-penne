(ns word-penne.components.navigation
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.subs :as subs]
            [word-penne.events :as events]))

(def s-navigation
  {:height "100%"
   :padding-top ".5rem"
   :border-right (str "1px solid " (:assort-border color))
   :background (:main-background color)
   :overflow-x "hidden"
   :transition "0.5s"})
(def s-opened-navigation
  {:width (:navigation-width layout-vars)})
(def s-closed-navigation
  {:width (:closed-navigation-width layout-vars)})
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
(def s-nav-close-icon
  {:margin-top ".2rem"
   :margin-right ".3rem"})
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
  {:margin-top ".2rem"
   :margin-right ".3rem"})

; https://www.w3schools.com/howto/howto_js_off-canvas.asp
; TODO sidnav の開閉
(defn Navigation []
  [:nav (use-style s-navigation {:id "sidenav"})
   (if @(re-frame/subscribe [::subs/show-navigation])
     [:div (use-style s-opened-navigation)
      [:div (use-style s-nav-close)
       [:a (use-style s-nav-close-link {:href "#"
                                        :on-click #((re-frame/dispatch [::events/set-show-navigation false]))})
        [:span (use-style s-nav-close-icon {:class "material-icons-outlined"}) "chevron_left"]]]
      [:a (use-style s-nav-link {:href "#"}) "tag1"]
      [:a (use-style s-nav-link {:href "#"}) "tag2"]
      [:a (use-style s-nav-link {:href "#"}) "tag3"]]
     [:div (use-style s-closed-navigation)
      [:div (use-style s-nav-open)
       [:a (use-style s-nav-open-link {:href "#"
                                       :on-click #(re-frame/dispatch [::events/set-show-navigation true])})
        [:span (use-style s-nav-open-icon {:class "material-icons-outlined"}) "chevron_right"]]]])])
