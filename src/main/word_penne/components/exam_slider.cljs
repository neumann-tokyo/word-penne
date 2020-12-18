(ns word-penne.components.exam-slider
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]))

(def s-slider
  {:width "100%"
   :height "100%"
   :display "flex"
   :overflow-x "auto"
   :scroll-snap-type "x mandatory"
   :scroll-behavior "smooth"})
(def s-slide
  {:width "100%"
   :height "100%"
   :flex-shrink "0"
   :scroll-snap-align "start"})

(defn ExamSlider []
  [:div (use-style s-slider)
   [:div (use-style s-slide {:id "s1"})
    [:span "1"]
    [:a {:href "#s2"} ">>"]]
   [:div (use-style s-slide)
    [:a {:name "s2"}]
    [:span "2"]
    [:a {:href "#s3"} ">>"]]
   [:div (use-style s-slide {:id "s3"})
    [:span "3"]
    [:a {:href "#s4"} ">>"]]
   [:div (use-style s-slide {:id "s4"})
    [:span "4"]
    [:a {:href (path-for routes :word-penne.pages.home/home)} "TOP"]]])
