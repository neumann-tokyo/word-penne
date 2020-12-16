(ns word-penne.pages.home
  (:require [word-penne.views :as v]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]))

(defmethod v/view ::home [_]
  [:div
   [WordCardsWrap]])
