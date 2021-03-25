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
   :margin-bottom "2rem"
   :column-width (:word-card-width layout-vars)
   :background (:main-background color)
   ::stylefy/media {phone-width {:flex-direction "column"}}})
(def s-card-item
  {:break-inside "avoid"
   :margin ".5rem"
   :display "inline-block"})

(defn- word-card-container [target]
  (when-let [cards (seq @(re-frame/subscribe target))]
    [:div (use-style s-cards-wrap)
     (doall (for [card cards]
              [:div (use-style s-card-item {:key (:uid card)})
               [WordCard card]]))]))

(defn WordCardsWrap []
  [:<>
   (word-card-container [::subs/locked-cards])
   (word-card-container [::subs/unlocked-cards])])
