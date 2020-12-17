(ns word-penne.components.word-cards-wrap
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.components.word-card :refer [WordCard]]
            [word-penne.style.vars :refer [color phone-width]]))

(def s-cards-wrap
  {:width "100%"
   :height "100%"
   :display "flex"
   :flex-wrap "wrap"
   :justify-content "flex-start"
   :align-content "flex-start"
   :align-items "center"
   :background (:main-background color)
   ::stylefy/media {phone-width {:flex-direction "column"}}})
(def s-card-item
  {:margin ".5rem"
   :display "inline-block"})

(defn WordCardsWrap []
  [:div (use-style s-cards-wrap)
   [:div (use-style s-card-item)
    [WordCard {:front-text "make"
               :back-text "作る"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "create"
               :back-text "作る"
               :comment "新しく作ること"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "have"
               :back-text "持つ"
               :comment "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "give"
               :back-text "あげる"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "bbbbbbbbbbbbbbbbbbbbbbbbb"
               :back-text "受け取る"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "do"
               :back-text "長いメッセージ長いメッセージ長いメッセージ長いメッセージ長いメッセージ"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
               :back-text "長いメッセージ長いメッセージ長いメッセージ長いメッセージ"
               :comment "長いメッセージ長いメッセージ長いメッセージ長いメッセージ"}]]
   [:div (use-style s-card-item)
    [WordCard {:front-text "you"
               :back-text "あなた"}]]])
