(ns word-penne.pages.home
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]
            [word-penne.events :as events]
            [word-penne.style.vars :refer [color]]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]
            [word-penne.components.confirmation-modal :refer [ConfirmationModal]]
            [word-penne.components.toggle-switch :refer [ToggleSwitch]]))

(def s-container
  {:display "flex"
   :justify-content "space-between"
   :align-items "center"
   :margin-right ".5rem"})
(def s-top-right
  {:display "inline-flex"
   :justify-content "flex-end"
   :align-items "center"})
(def s-cards-order-container
  {:background (:assort-background color)
   :border-radius "10px"
   :margin-right ".5rem"
   :display "inline-flex"
   :align-items "center"})
(def s-cards-order
  {:border "none"
   :background "none"
   :outline "none"
   :margin "5px"})

(defmethod v/view ::home [_]
  @(re-frame/subscribe [::subs/locale])
  [:div
   [:div (use-style s-container)
    [Button {:kind "secondary" :on-click (fn [e]
                                           (.preventDefault e)
                                           (re-frame/dispatch [::events/setup-quiz]))} (tr "Quiz")]
    [:div (use-style s-top-right)
     [:span (use-style s-cards-order-container)
      [:span {:class "material-icons-outlined"} "sort"]
      [:select (use-style s-cards-order {:name "cards-order"
                                         :id "cards-order"
                                         :value @(re-frame/subscribe [::subs/cards-order])
                                         :on-change #(re-frame/dispatch [::events/set-cards-order (-> % .-target .-value)])})
       [:option {:value "wrongRate/desc"} (tr "Wrong rate")]
       [:option {:value "updatedAt/desc"} (tr "Update")]
       [:option {:value "random/asc"} (tr "Random")]]]
     [ToggleSwitch {:on-change (fn [_]
                                 (re-frame/dispatch [::events/set-reverse-cards (not @(re-frame/subscribe [::subs/reverse-cards]))]))} (tr "Reverse")]]]
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
