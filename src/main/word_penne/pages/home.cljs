(ns word-penne.pages.home
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :refer [path-for]]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]
            [word-penne.events :as events]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color]]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.word-cards-wrap :refer [WordCardsWrap]]
            [word-penne.components.confirmation-modal :refer [ConfirmationModal]]
            [word-penne.components.toggle-switch :refer [ToggleSwitch]]
            [word-penne.components.usage-new-card :refer [UsageNewCard]]))

(def s-container
  {:display "flex"
   :justify-content "space-between"
   :align-items "center"
   :margin-right ".5rem"})
(def s-quiz-container
  {:display "flex"
   :align-items "center"
   :gap ".5rem"})
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
(def s-quiz-settings
  {:color (:accent-border color)})

(defmethod v/view ::home [_]
  @(re-frame/subscribe [::subs/locale])
  (let [cards-empty? (empty? @(re-frame/subscribe [::subs/cards]))]
    [:div
     [:div (use-style s-container)
      (if cards-empty?
        [:span ""]
        [:div (use-style s-quiz-container)
         [Button {:kind "secondary"
                  :on-click (fn [e]
                              (.preventDefault e)
                              (re-frame/dispatch [::events/setup-quiz]))} (tr "Quiz")]
         [:a (use-style s-quiz-settings {:href (path-for routes :word-penne.pages.user/quiz-settings)})
          [:span {:class "material-icons-outlined"} "settings"]]
         (when (<= (count @(re-frame/subscribe [::subs/cards])) 3)
           [:div
            [:span {:class "material-icons-outlined"} "arrow_upward"]
            (tr "Let's take the quiz and review the registered words!!")])])
      [:div (use-style s-top-right)
       [:span (use-style s-cards-order-container)
        [:span {:class "material-icons-outlined"} "sort"]
        [:select (use-style s-cards-order {:name "cards-order"
                                           :id "cards-order"
                                           :value @(re-frame/subscribe [::subs/cards-order])
                                           :on-change #(re-frame/dispatch [::events/set-cards-order (-> % .-target .-value)])})
         [:option {:value "updatedAt/desc"} (tr "Update")]
         [:option {:value "wrongRate/desc"} (tr "Wrong rate")]
         [:option {:value "random/asc"} (tr "Random")]]]
       [ToggleSwitch {:on-change (fn [_]
                                   (re-frame/dispatch [::events/set-reverse-cards (not @(re-frame/subscribe [::subs/reverse-cards]))]))} (tr "Reverse")]]]
     (when-let [tag @(re-frame/subscribe [::subs/search-tag])]
       [:p (str (tr "Tag: ") tag)])
     (when @(re-frame/subscribe [::subs/search-archive])
       [:p "Archive"])
     (if cards-empty?
       [UsageNewCard]
       [WordCardsWrap])
     [ConfirmationModal {:title (tr "Do you want to delete, really? This action don't return")
                         :ok-event (fn [e]
                                     (.preventDefault e)
                                     (re-frame/dispatch [::events/delete-selected-card])
                                     (re-frame/dispatch [::events/delete-card-by-uid (:uid @(re-frame/subscribe [::subs/selected-card]))]))
                         :cancel-event (fn [e]
                                         (.preventDefault e)
                                         (re-frame/dispatch [::events/delete-selected-card])
                                         (re-frame/dispatch [::events/hide-confirmation-modal]))}]]))
