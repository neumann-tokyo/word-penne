(ns word-penne.pages.cards
  (:require #_[stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.views :as v]
            [word-penne.components.word-card-form :refer [WordCardForm]]
            [word-penne.components.quiz-slider :refer [QuizSlider]]))

(defmethod v/view ::new [_]
  [:div
   [WordCardForm {:on-submit #(re-frame/dispatch [::events/create-card %])}]])

(defmethod v/view ::edit [_]
  (when-let [{:keys [uid front-text back-text comment]} @(re-frame/subscribe [::subs/selected-card])]
    [:div
     [WordCardForm {:initial-values {"uid" uid
                                     "front-text" front-text
                                     "back-text" back-text
                                     "comment" comment}
                    :on-submit #(re-frame/dispatch [::events/update-card-by-uid uid %])}]]))

(defmethod v/view ::quiz [_]
  [QuizSlider])
