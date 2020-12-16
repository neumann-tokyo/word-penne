(ns word-penne.pages.new-card
  (:require #_[stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.components.word-card-form :refer [WordCardForm]]))

(defmethod v/view ::new-card [_]
  [:div
   [WordCardForm]])
