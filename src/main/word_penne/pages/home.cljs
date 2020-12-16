(ns word-penne.pages.home
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.views :refer [view]]
            [word-penne.components.word-card :refer [WordCard]]))


(def s-cards-wrap
  {:width "100%"
   :height "100vh"
   :margin-top ".5rem"
   :display "flex"
   :flex-wrap "wrap"
   :justify-content "flex-start"
   :align-content "flex-start"})
(def s-card-item
  {:break-inside "avoid"
   :padding-bottom "1rem"
   :display "inline-block"})

(defmethod view ::home [_]
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
    [WordCard {:front-text "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
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
; [:a {:href (path-for routes :word-penne.views/list)} "list"]
