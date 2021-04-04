(ns word-penne.components.quiz-slide
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.style.share :as share]
            [word-penne.components.button :refer [Button]]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]))

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

(defn QuizSlide [{:keys [values handle-change handle-blur]} attrs]
  @(re-frame/subscribe [::subs/locale])
  (let [identifier (str "text-" (:index attrs))]
    [:div (use-style s-container)
     [:div (use-style s-card)
    ;; TODO js で .turned-flip をつける
      [:div (use-style s-card-inner)
       [:div (use-style s-card-front) (:front attrs)]
       [:div (use-style s-card-back) (:back attrs)]]]
     [:div (use-style s-input)
      [:div
       [:input (use-style s-text {:type "text"
                                  :id identifier
                                  :name identifier
                                  :value (values identifier)
                                  :on-change handle-change
                                  :on-blur handle-blur})]]
      [:div (use-style s-buttons-container)
     ;;[:span (use-style s-buttons-wrap) [Button {:href "#"} "I don't know"]]
       [:span (use-style s-buttons-wrap)
        [Button {:href "#"
                 :kind "secondary"
                 :on-click (fn [e]
                             ;; TODO (values identifier) と (:back attrs) を比べて一致すれば正解、不一致なら不正解
                             ;; 正誤とカードの表示のロジックをどうするか
                             (prn (values identifier))
                             (.preventDefault e))} (tr "OK")]]]]]))
