(ns word-penne.components.judgement-mark
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color]]))

(def s-correct
  {:color (:good-card-text color)})
(def s-wrong
  {:color (:bad-card-text color)})

(defn JudgementMark [text]
  (if (= text "Correct")
    [:span (use-style s-correct {:class "material-icons-outlined"}) "check_circle"]
    [:span (use-style s-wrong {:class "material-icons-outlined"}) "cancel"]))
