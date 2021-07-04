(ns word-penne.components.usage-new-card
  (:require [re-frame.core :as re-frame]
            [word-penne.subs :as subs]))

(defn UsageNewCard []
  [:div
   (if (= @(re-frame/subscribe [::subs/locale]) "ja")
     [:div "日本語 ようこそ!"]
     [:div "English Welcome!"])])
