(ns word-penne.components.quiz-slide
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.style.share :as share]
            [word-penne.components.button :refer [Button]]))

(def s-container
  {})
(def s-card
  {:background-color "transparent"
   :width "40%"
   :margin "1rem auto"
   :height "max-content"
   :perspective "1000px"
   ::stylefy/media {phone-width {:width "85vw"
                                 :margin "1rem 0"}}})
(stylefy/class "turned-flip"
               {:transform "rotateY(180deg)"
                :border "none"})
(def s-card-inner
  {:display "grid"
   :width "100%"
   :height "100%"
   :grid-template-columns "1fr"
   :transition "transform 0.6s"
   :transform-style "preserve-3d"})
(def m-card-design
  {:width "100%"
   :min-height "10rem"
   :-webkit-backface-visibility "hidden"
   :backface-visibility "hidden"
   :text-align "center"
   :font-size "2rem"
   :grid-column 1
   :grid-row 1
   :line-height "10rem"
   :font-weight "bold"})
(def s-card-front
  (merge share/m-card
         m-card-design))
(def s-card-back
  (merge share/m-card
         m-card-design
         {:transform "rotateY(180deg)"}))
(def s-input
  {:width "40%"
   :margin "1rem auto"
   ::stylefy/media {phone-width {:width "85vw"
                                 :margin "1rem 0"}}})
(def s-text
  {:width "100%"
   :padding ".5rem"
   :margin ".5rem 0"
   :box-sizing "border-box"
   :outline "none"
   :border (str "solid 1px " (:assort-border color))
   :border-radius ".5rem"})
(def s-buttons-container
  {:text-align "right"})
(def s-buttons-wrap
  {:margin-left ".5rem"})

(defn QuizSlide [attrs]
  [:div (use-style s-container)
   [:div (use-style s-card)
    ;; TODO js で .turned-flip をつける
    [:div (use-style s-card-inner)
     [:div (use-style s-card-front) (:front-text attrs)]
     [:div (use-style s-card-back) (:back-text attrs)]]]
   [:div (use-style s-input)
    [:div
     [:input (use-style s-text {:type "text" :id (str "text-" (:id attrs)) :name (str "text-" (:id attrs))})]]
    [:div (use-style s-buttons-container)
     [:span (use-style s-buttons-wrap) [Button {:href "#"} "I don't know"]]
     [:span (use-style s-buttons-wrap) [Button {:href "#" :kind "secondary"} "OK"]]]]])
