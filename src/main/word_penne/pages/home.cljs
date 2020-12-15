(ns word-penne.pages.home
  (:require #_[stylefy.core :as stylefy :refer [use-style use-sub-style]]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.views :refer [view]]
            [word-penne.components.word-card :refer [WordCard]]))

(defmethod view ::home [_]
  [:div
   [:div "Home"]
   [:div
    [WordCard {:front-text "make"
               :back-text "作る"}]
    [WordCard {:front-text "create"
               :back-text "作る"
               :comment "新しく作ること"}]
    [WordCard {:front-text "have"
               :back-text "持つ"}]
    [WordCard {:front-text "give"
               :back-text "あげる"}]]])
; [:a {:href (path-for routes :word-penne.views/list)} "list"]
