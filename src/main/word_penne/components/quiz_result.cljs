(ns word-penne.components.quiz-result
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
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
(def s-judgement
  {:display "flex"
   :align-items "center"})
(def s-buttons-container
  {:text-align "right"
   :margin-top "1rem"})

(defn QuizResult []
  @(re-frame/subscribe [::subs/locale])
  (let [cards @(re-frame/subscribe [::subs/quiz-cards])]
    [:div (use-style s-container)
     [:h3 (tr "Result")]
     [:table (use-style s-table)
      [:thead
       [:tr
        [:th (use-style s-table-header) (tr "Quiz")]
        [:th (use-style s-table-header) (tr "Answer")]
        [:th (use-style s-table-header) (tr "Result")]]]
      [:tbody
       (doall (map-indexed
               (fn [i card]
                 ^{:key i} [:tr
                            [:td (use-style s-table-row) (:front card)]
                            [:td (use-style s-table-row) (:back card)]
                            [:td (use-style s-table-row)
                             [:div (use-style s-judgement)
                              [JudgementMark (:judgement card)]
                              [:span (tr (:judgement card))]]]])
               cards))]]
     [:div (use-style s-buttons-container)
      [Button {:kind "primary" :href (path-for routes :word-penne.pages.home/home)} (tr "Finish")]]]))
