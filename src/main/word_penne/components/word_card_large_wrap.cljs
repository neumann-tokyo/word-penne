(ns word-penne.components.word-card-large-wrap
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.style.vars :refer [layout-vars color phone-width]]
            [word-penne.i18n :refer [tr]]
            [word-penne.subs :as subs]
            [word-penne.events :as events]
            [word-penne.components.confirmation-modal :refer [ConfirmationModal]]
            [word-penne.components.word-card-large :refer [WordCardLarge]]))

(def s-container
  {:width "80%"
   :margin "0 auto"
   ::stylefy/media {phone-width {:width "95%"}}})
(def s-cards-wrap
  {:width "100%"
   :height "100%"
   :column-count "auto"
   :margin "2rem 0"
   :column-width (:word-card-width layout-vars)
   :background (:main-background color)
   ::stylefy/media {phone-width {:margin-top "1rem"}}})
(def s-card-wrap
  {:padding-top "1rem"})

(defn WordCardLargeWrap [card]
  @(re-frame/subscribe [::subs/locale])
  [:<>
   [:div (use-style s-container)
    [WordCardLarge {} card]
    [:div (use-style s-cards-wrap)
     (doall
      (for [relational-card @(re-frame/subscribe [::subs/relational-cards])]
        [:div (use-style s-card-wrap {:key (:uid relational-card)})
         [WordCardLarge {} relational-card]]))]]
   ;; TODO homeとコードが重複している
   [ConfirmationModal {:title (tr "Do you want to delete, really? This action don't return")
                       :ok-event (fn [e]
                                   (.preventDefault e)
                                   (re-frame/dispatch [::events/delete-selected-card])
                                   (re-frame/dispatch [::events/delete-card-by-uid (:uid @(re-frame/subscribe [::subs/selected-card]))])
                                   (re-frame/dispatch [::events/navigate :word-penne.pages.home/home]))
                       :cancel-event (fn [e]
                                       (.preventDefault e)
                                       (re-frame/dispatch [::events/delete-selected-card])
                                       (re-frame/dispatch [::events/hide-confirmation-modal]))}]])
