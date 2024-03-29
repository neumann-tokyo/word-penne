(ns word-penne.pages.cards
  (:require [re-frame.core :as re-frame]
            [word-penne.i18n :refer [tr]]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.views :as v]
            [word-penne.components.word-card-form :refer [WordCardForm]]
            [word-penne.components.quiz-form :refer [QuizForm]]
            [word-penne.components.quiz-result :refer [QuizResult]]
            [word-penne.components.confirmation-modal :refer [ConfirmationModal]]
            [word-penne.components.word-card-large-wrap :refer [WordCardLargeWrap]]))

(defmethod v/view ::new [_]
  [:div
   [WordCardForm {:on-submit #(re-frame/dispatch [::events/create-card %])}]])

(defmethod v/view ::show []
  [:div
   (when-let [card @(re-frame/subscribe [::subs/selected-card])]
     [WordCardLargeWrap card])])

(defmethod v/view ::edit [_]
  [:div
   (when-let [{:keys [uid front back comment tags]} @(re-frame/subscribe [::subs/selected-card])]
     [WordCardForm {:initial-values {"uid" uid
                                     "front" front
                                     "back" back
                                     "comment" comment
                                     "tags" (mapv (fn [t] {"name" t "beforeName" t}) tags)}
                    :on-submit #(re-frame/dispatch [::events/update-card-by-uid uid %])}])])

(defmethod v/view ::quiz [_]
  @(re-frame/subscribe [::subs/locale])
  [:<>
   (if (< @(re-frame/subscribe [::subs/quiz-pointer])
          (count @(re-frame/subscribe [::subs/quiz-cards])))
     [QuizForm]
     [QuizResult])
   [ConfirmationModal {:title (tr "Do you quit? The data in the middle will be deleted.")
                       :ok-event (fn [e]
                                   (.preventDefault e)
                                   (re-frame/dispatch [::events/navigate :word-penne.pages.home/home])
                                   (re-frame/dispatch [::events/hide-confirmation-modal]))
                       :cancel-event (fn [e]
                                       (.preventDefault e)
                                       (re-frame/dispatch [::events/hide-confirmation-modal]))}]])
