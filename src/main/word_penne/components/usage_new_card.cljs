(ns word-penne.components.usage-new-card
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.subs :as subs]
            [word-penne.components.word-card-add-button :refer [WordCardAddButton]]))

(def s-container
  {:text-align "center"})
(def s-text-with-add-button
  {:display "flex"
   :align-items "center"
   :justify-content "center"})

(defn UsageNewCard []
  [:div (use-style s-container)
   (if (= @(re-frame/subscribe [::subs/locale]) "ja")
     [:div
      [:h2 "ようこそ！WordPenneへ！"]
      [:p "ご登録いただきありがとうございます。"]
      [:p (use-style s-text-with-add-button)
       "まずは"
       [WordCardAddButton]
       "ボタンを押下して最初の単語帳カードを登録しましょう！"]]
     [:div
      [:h2 "Welcome! WordPenne!"]
      [:p "Thank you for registration"]
      [:p (use-style s-text-with-add-button)
       "Let's push "
       [WordCardAddButton]
       " button and add your first flash card!!"]])])
