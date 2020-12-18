(ns word-penne.pages.cards
  (:require #_[stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.components.word-card-form :refer [WordCardForm]]
            [word-penne.components.exam-slider :refer [ExamSlider]]))

(defmethod v/view ::new [_]
  [:div
   [WordCardForm]])

(defmethod v/view ::edit [_]
  [:div
   [WordCardForm]])

(defmethod v/view ::test [_]
  [ExamSlider])
