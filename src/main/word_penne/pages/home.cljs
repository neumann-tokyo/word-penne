(ns word-penne.pages.home
  (:require [word-penne.views :as v]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]))

(defmethod v/view ::home [_]
  [:div
   [:div
    [:span "Tag1"]
    ;; TODO mobile では bottom navigation で Test を出したい
    [Button {:kind "secondary" :href "#"} "Test"]]
   [WordCardsWrap]])
