(ns word-penne.components.delete-card-modal
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.subs :as subs]
            [word-penne.style.vars :refer [z-indexs color phone-width]]
            [word-penne.events :as events]
            [word-penne.components.button :refer [Button]]))

(def s-modal
  {:position "fixed"
   :z-index (:delete-card-modal z-indexs)
   :left 0
   :top 0
   :width "100%"
   :height "100%"
   :overflow "auto"
   :background-color "rgba(0,0,0,0.4)"})

(def s-modal-content
  {:background-color (:main-background color)
   :margin "0 auto"
   :padding "20px"
   :border (str "1px solid " (:assort-border color))
   :border-radius "0 0 1rem 1rem"
   :width "33%"
   ::stylefy/media {phone-width {:width "90%"}}})

(def s-button-container
  {:text-align "right"})

(defn DeleteCardModal []
  [:div
   (when @(re-frame/subscribe [::subs/show-delete-card-modal])
     [:div (use-style s-modal)
      [:div (use-style s-modal-content)
       [:h3 "Confirmation"]
       [:p "Do you want to delete, really? This action don't return"]
       [:div (use-style s-button-container)
        [Button {:href "#" :kind "primary"} "OK"]
        [Button {:href "#"
                 :kind "secondary"
                 :on-click (fn [e]
                             (.preventDefault e)
                             (re-frame/dispatch [::events/hide-delete-card-modal]))} "Cancel"]]]])])
