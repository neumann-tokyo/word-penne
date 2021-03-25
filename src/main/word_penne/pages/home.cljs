(ns word-penne.pages.home
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]
            [word-penne.components.delete-card-modal :refer [DeleteCardModal]]))

(defmethod v/view ::home [_]
  [:div
   [:div
    ;; TODO mobile では bottom navigation で quiz を出したい
    [Button {:kind "secondary" :href (path-for routes :word-penne.pages.cards/quiz)} "Quiz"]]
   (when-let [tag @(re-frame/subscribe [::subs/search-tag])]
     [:p (str "Tag: " tag)])
   (when @(re-frame/subscribe [::subs/search-archive])
     [:p "Archive"])
   [WordCardsWrap]
   [DeleteCardModal]])
