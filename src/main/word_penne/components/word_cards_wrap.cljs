(ns word-penne.components.word-cards-wrap
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.components.word-card :refer [WordCard]]
            [word-penne.style.vars :refer [color layout-vars phone-width]]))

(def s-cards-wrap
  {:width "100%"
   :height "100%"
   :column-count "auto"
   :column-width (:word-card-width layout-vars)
   :background (:main-background color)
   ::stylefy/media {phone-width {:flex-direction "column"}}})
(def s-card-item
  {:break-inside "avoid"
   :margin ".5rem"
   :display "inline-block"})

(defn WordCardsWrap []
  [:div (use-style s-cards-wrap)
   (doall (for [card @(re-frame/subscribe [::subs/cards])]
            [:div (use-style s-card-item {:key (:uid card)})
             [WordCard (select-keys card [:uid :front-text :back-text :comment])]]))])
