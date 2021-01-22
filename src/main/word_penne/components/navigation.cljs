(ns word-penne.components.navigation
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :refer [path-for]]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color layout-vars]]
            [word-penne.subs :as subs]
            [word-penne.events :as events]
            [word-penne.routes :refer [routes]]))

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
   :color (:main-text color)
   :display "flex"
   :flex-direction "row"
   :align-items "center"
   ::stylefy/mode {:hover {:background (:assort-background color)
                           :border-radius "0 2rem 2rem 0"}}})
(def s-nav-icon
  {:margin-top ".3rem"
   :margin-left ".3rem"})
(def s-nav-link-text
  {:margin-left "1rem"})

;; Pure CSS Hamburger menu
;; https://codepen.io/erikterwan/pen/EVzeRP
(defn Navigation []
  [:nav (use-style s-navigation)
   [:input (use-style s-navigation-checkbox {:type "checkbox" :id "navigation-menu-checkbox" :name "navigation-menu-checkbox"})]
   [:label {:for "navigation-menu-checkbox"}
    [:span (use-style s-nav-icon {:class "material-icons-outlined"}) "menu"]]
   [:div#navigation-menu (use-style s-opened-navigation)
    [:div (use-style s-nav-close)]
    [:a (use-style s-nav-link {:href "#"
                               :on-click (fn [e]
                                           (.preventDefault e)
                                           (re-frame/dispatch [::events/set-search-tag nil])
                                           (re-frame/dispatch [::events/navigate :word-penne.pages.home/home]))})
     [:span {:class "material-icons-outlined"} "style"]
     [:span (use-style s-nav-link-text) "Cards"]]
    (doall (map-indexed
            (fn [index tag]
              [:a (use-style s-nav-link {:href "#" :key index
                                         :on-click (fn [e]
                                                     (.preventDefault e)
                                                     (re-frame/dispatch [::events/set-search-tag tag]))})
               [:span {:class "material-icons-outlined"} "label"]
               [:span (use-style s-nav-link-text) tag]])
            @(re-frame/subscribe [::subs/tags])))
    [:a (use-style s-nav-link {:href (path-for routes :word-penne.pages.tags/index)})
     [:span {:class "material-icons-outlined"} "edit"]
     [:span (use-style s-nav-link-text) "Edit tags"]]
    [:a (use-style s-nav-link {:href "#"})
     [:span {:class "material-icons-outlined"} "archive"]
     [:span (use-style s-nav-link-text) "Archive"]]]])
