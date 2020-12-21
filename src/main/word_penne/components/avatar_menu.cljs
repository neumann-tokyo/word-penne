(ns word-penne.components.avatar-menu
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.style.vars :refer [color z-indexs]]))

(def s-menu
  {:position "relative"})
(def s-menu-button
  {:maring 0
   :padding 0
   :background (:main-background color)
   :outline "none"
   :border "none"
   ::stylefy/manual [[:&:focus-within [:#avatar-menu {:display "block"}]]]})
(def s-avatar
  {:vertical-align "middle"
   :width "2.5rem"
   :height "2.5rem"
   :border-radius "50%"
   ::stylefy/mode {:hover {:filter "brightness(90%)"}}})
(def s-menu-content
  {:display "none"
   :position "absolute"
   :right 0
   :z-index (:avatar-menu z-indexs)
   :background (:main-background color)
   :border (str "solid 1px " (:assort-border color))
   :border-radius "1rem"
   :color (:main-text color)
   :width "7rem"
   :box-shadow (str "0 2px 4px 0 " (:assort-border color))
   :overflow "hidden"})
(def s-menu-link
  {:display "block"
   :color (:main-text color)
   :text-decoration "none"
   :padding ".5rem"
   ::stylefy/mode {:hover {:background (:assort-background color)}}})

(defn AvatarMenu []
  [:div (use-style s-menu)
   [:button (use-style s-menu-button)
    (let [{:keys [photo-url display-name]} @(re-frame/subscribe [::subs/current-user])]
      [:img (use-style s-avatar {:src photo-url :alt display-name})])
    [:div#avatar-menu (use-style s-menu-content)
     [:a (use-style s-menu-link {:href "#"}) "Setting"]
     [:a (use-style s-menu-link {:href "#"}) "xxx"]
     [:a (use-style s-menu-link {:href "#"}) "Logout"]]]])
