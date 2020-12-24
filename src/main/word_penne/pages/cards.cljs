(ns word-penne.pages.cards
  (:require #_[stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.events :as events]
            [word-penne.views :as v]
            [word-penne.components.word-card-form :refer [WordCardForm]]
            [word-penne.components.quiz-slider :refer [QuizSlider]]))

(defmethod v/view ::new [_]
  [:div
   [WordCardForm {:on-submit #(re-frame/dispatch [::events/create-card %])}]])

(defmethod v/view ::edit [_]
  [:div
   [WordCardForm]])

(defmethod v/view ::quiz [_]
  [QuizSlider])
