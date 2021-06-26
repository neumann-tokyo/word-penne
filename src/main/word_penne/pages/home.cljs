(ns word-penne.pages.home
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]
            [word-penne.events :as events]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]
            [word-penne.components.confirmation-modal :refer [ConfirmationModal]]
            [word-penne.components.toggle-switch :refer [ToggleSwitch]]))

(def s-container
  {:display "flex"
   :justify-content "space-between"
   :align-items "center"
   :margin-right ".5rem"})

(defmethod v/view ::home [_]
  @(re-frame/subscribe [::subs/locale])
  [:div
   [:div (use-style s-container)
    [Button {:kind "secondary" :on-click (fn [e]
                                           (.preventDefault e)
                                           (re-frame/dispatch [::events/setup-quiz]))} (tr "Quiz")]
    [ToggleSwitch {:on-change (fn [e]
                                (js/console.log "hihihi"))} (tr "Reverse")]]
   (when-let [tag @(re-frame/subscribe [::subs/search-tag])]
     [:p (str (tr "Tag: ") tag)])
   (when @(re-frame/subscribe [::subs/search-archive])
     [:p "Archive"])
   [WordCardsWrap]
   [ConfirmationModal {:title (tr "Do you want to delete, really? This action don't return")
                       :ok-event (fn [e]
                                   (.preventDefault e)
                                   (re-frame/dispatch [::events/delete-selected-card])
                                   (re-frame/dispatch [::events/delete-card-by-uid (:uid @(re-frame/subscribe [::subs/selected-card]))]))
                       :cancel-event (fn [e]
                                       (.preventDefault e)
                                       (re-frame/dispatch [::events/delete-selected-card])
                                       (re-frame/dispatch [::events/hide-confirmation-modal]))}]])
