(ns word-penne.components.quiz-result
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :refer [path-for]]
            [clojure.string :as str]
            [stylefy.core :as stylefy :refer [use-style]]
            ["pure-react-carousel" :refer [ButtonNext]]
            [word-penne.routes :refer [routes]]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.style.share :as share]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.judgement-mark :refer [JudgementMark]]
            [word-penne.subs :as subs]
            [word-penne.style.vars :refer [color]]
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

(defn QuizResult [{:keys [values handle-change handle-blur set-values]} {:keys [cards]}]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style s-container)
   [:h3 (tr "Result")]
   [:table (use-style s-table)
    [:tr
     [:th (use-style s-table-header) (tr "Quiz")]
     [:th (use-style s-table-header) (tr "Answer")]
     [:th (use-style s-table-header) (tr "Result")]]
    (doall (map-indexed
            (fn [i card]
              ^{:index i} [:tr
                           [:td (use-style s-table-row) (:front card)]
                           [:td (use-style s-table-row) (:back card)]
                           [:td (use-style s-table-row)
                            (let [judgement (some-> (str "judgement-" i)
                                                    values
                                                    tr)]
                              [:div (use-style s-judgement)
                               [JudgementMark judgement]
                               [:span judgement]])]])
            cards))]
   [:div (use-style s-buttons-container)
    [Button {:kind "secondary" :href (path-for routes :word-penne.pages.home/home)} (tr "Finish")]]])

