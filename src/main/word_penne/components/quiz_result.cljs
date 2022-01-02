(ns word-penne.components.quiz-result
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.events :as events]
            [word-penne.style.vars :refer [color]]
            [word-penne.style.share :as share]
            [word-penne.routes :refer [routes]]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.judgement-mark :refer [JudgementMark]]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]))

(def s-container
  (merge share/m-card
         {:width "85%"
          :margin "1rem auto"
          :height "max-content"}))
(def s-table
  {:width "100%"
   :border "none"
   :border-collapse "collapse"
   :border-spacing 0})
(def s-table-header
  {:border-bottom (str "solid 1px " (:assort-border color))})
(def s-table-row
  {:padding ".5rem 0"
   :border-bottom (str "solid 1px " (:assort-border color))})
(def s-table-row-text-center
  {:padding ".5rem 0"
   :border-bottom (str "solid 1px " (:assort-border color))
   :text-align "center"})
(def s-judgement
  {:display "flex"
   :align-items "center"
   :justify-content "center"})
(def s-buttons-container
  {:margin-top "1rem"
   :display "flex"
   :justify-content "flex-end"
   :gap ".5rem"})
(def s-edit-button
  {:color (:main-text color)})

(defn QuizResult []
  @(re-frame/subscribe [::subs/locale])
  (let [cards @(re-frame/subscribe [::subs/quiz-cards])]
    [:div (use-style s-container)
     [:h3 (tr "Result")]
     (if (seq cards)
       [:table (use-style s-table)
        [:thead
         [:tr
          [:th (use-style s-table-header) (tr "Quiz")]
          [:th (use-style s-table-header) (tr "Answer")]
          [:th (use-style s-table-header) (tr "Result")]
          [:th (use-style s-table-header) (tr "Edit")]]]
        [:tbody
         (doall (map-indexed
                 (fn [i card]
                   ^{:key i} [:tr
                              [:td (use-style s-table-row) (:front card)]
                              [:td (use-style s-table-row) (:back card)]
                              [:td (use-style s-table-row)
                               [:div (use-style s-judgement)
                                [JudgementMark (:judgement card)]
                                [:span (tr (:judgement card))]]]
                              [:td (use-style s-table-row-text-center)
                               [:a (use-style s-edit-button {:href "#"
                                                             :on-click (fn [e]
                                                                         (.preventDefault e)
                                                                         (re-frame/dispatch [::events/archive-card (:uid card) true]))
                                                             :title "archive"})
                                [:span {:class "material-icons-outlined"} "archive"]]
                               [:a (use-style s-edit-button
                                              {:href "#"
                                               :title "edit"
                                               :on-click (fn [e]
                                                           (.preventDefault e)
                                                           (re-frame/dispatch [::events/navigate :word-penne.pages.cards/edit {:id (:uid card)}]))})
                                [:span {:class "material-icons-outlined"} "edit"]]]])
                 cards))]]
      ;;  TODO ちゃんとしたメッセージにする
       [:div "Oops! クイズを作成できませんでした。設定を見直してみてください。[クイズ設定]"])
     [:div (use-style s-buttons-container)
      [Button {:kind "secondary"
               :on-click (fn [e]
                           (.preventDefault e)
                           (re-frame/dispatch [::events/setup-quiz]))} (tr "Retry")]
      [Button {:kind "primary" :href (path-for routes :word-penne.pages.home/home)} (tr "Finish")]]]))
