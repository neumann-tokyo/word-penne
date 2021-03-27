(ns word-penne.components.avatar-menu
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.events :as events]
            [word-penne.style.vars :refer [color z-indexs]]
            [word-penne.i18n :refer [i18n]]))

(def s-menu
  {:position "relative"})
(def s-menu-button
  {:maring 0
   :padding 0
   :background (:main-background color)
   :outline "none"
   :border "none"
   :cursor "pointer"
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
   :min-width "7rem"
   :box-shadow (str "0 2px 4px 0 " (:assort-border color))
   :overflow "hidden"})
(def s-menu-link
  {:display "block"
   :color (:main-text color)
   :text-decoration "none"
   :padding ".5rem"
   ::stylefy/mode {:hover {:background (:assort-background color)}}})

(defn AvatarMenu []
  ;; FIXME ここで locale を呼ばないと日本語をロードできない
  (prn @(re-frame/subscribe [::subs/locale]))
  (prn (.gettext (i18n) "Settings"))
  (let [{:keys [photo-url email]} @(re-frame/subscribe [::subs/current-user])]
    [:div (use-style s-menu)
     [:button (use-style s-menu-button)
      [:img (use-style s-avatar {:src photo-url :alt email})]
      [:div#avatar-menu (use-style s-menu-content)
       [:a (use-style s-menu-link) email]
       [:a (use-style s-menu-link {:href "#"
                                   :on-click (fn [e]
                                               (.preventDefault e)
                                               (re-frame/dispatch [::events/navigate :word-penne.pages.user/edit]))})
        (.gettext (i18n) "Settings")]
       [:a (use-style s-menu-link {:href "#" :on-click (fn [e]
                                                         (.preventDefault e)
                                                         (re-frame/dispatch [::events/signout]))})
        (.gettext (i18n) "Logout")]]]]))
