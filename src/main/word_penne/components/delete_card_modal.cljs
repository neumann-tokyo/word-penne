(ns word-penne.components.delete-card-modal
  (:require [re-frame.core :as re-frame]
            [word-penne.subs :as subs]))

; TODO :show-delete-modal true のときにモーダルを表示したい
(defn DeleteCardModal []
  [:div
   (when @(re-frame/subscribe [::subs/show-delete-card-modal])
     [:div "true"])])
