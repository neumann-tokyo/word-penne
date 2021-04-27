(ns word-penne.components.judgement-mark
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def s-correct
  {:color "#76cf4a"})
(def s-wrong
  {:color "#cf534a"})

(defn JudgementMark [text]
  (if (= text "Correct")
    [:span (use-style s-correct {:class "material-icons-outlined"}) "check_circle"]
    [:span (use-style s-wrong {:class "material-icons-outlined"}) "cancel"]))
