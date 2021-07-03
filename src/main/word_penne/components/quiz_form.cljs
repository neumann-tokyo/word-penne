(ns word-penne.components.quiz-form
  (:require [re-frame.core :as re-frame]
            [fork.reagent :as fork]
            [stylefy.core :as stylefy :refer [use-style]]
            [clojure.string :as str]
            [word-penne.events :as events]
            [word-penne.i18n :refer [tr]]
            [word-penne.subs :as subs]
            [word-penne.style.form :as sf]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.style.share :as share]
            [word-penne.components.judgement-mark :refer [JudgementMark]]
            [word-penne.components.button :refer [Button]]))

(def s-header
  {:text-align "right"
   :margin "2rem"})
(def s-cancel-link
  {:color (:title-text color)})
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
(stylefy/class "fadein"
               {:animation-name "fadein"
                :animation-duration "2s"})
(stylefy/keyframes "fadein"
                   [:from
                    {:opacity 0
                     :transform "translateY(20px)"}]
                   [:to
                    {:opacity 1
                     :transform "translateY(0)"}])
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
   :padding-top "4rem"
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
  {:display "flex"
   :align-items "center"
   :justify-content "flex-end"})
(def s-buttons-wrap
  {:margin-left ".5rem"})

(defn- next-page-action [set-values]
  (re-frame/dispatch-sync [::events/increment-quiz-pointer])
  (let [card @(re-frame/subscribe [::subs/quiz-card])]
    (set-values {"answer" nil
                 "correct-text" (:back card)
                 "uid" (:uid card)})))

(defn QuizForm []
  @(re-frame/subscribe [::subs/locale])
  (let [card @(re-frame/subscribe [::subs/quiz-card])
        before-answer? (str/blank? (:judgement card))]
    [:div
     [:div (use-style s-header)
      [:a (use-style s-cancel-link {:href "#"
                                    :title "cancel"
                                    :on-click (fn [e]
                                                (.preventDefault e)
                                                (re-frame/dispatch [::events/show-confirmation-modal]))})
       [:span {:class "material-icons-outlined"} "cancel"]]]

     [:div (use-style s-container)
      [:div (use-style s-card)
       [:div (use-style s-card-inner {:class (if before-answer? "fadein" "turned-flip")})
        [:div (use-style s-card-front) (:front card)]
        [:div (use-style s-card-back) (:back card)]]]
      [fork/form {:path [:form]
                  :form-id "quiz-answer-form"
                  :prevent-default? true
                  :clean-on-unmount? true
                  :initial-values {"answer" nil
                                   "correct-text" (:back card)
                                   "uid" (:uid card)}
                  :on-submit #(re-frame/dispatch [::events/answer-quiz %])}
       (fn [{:keys [values
                    form-id
                    set-values
                    handle-change
                    handle-blur
                    submitting?
                    handle-submit]}]
         [:form {:id form-id :on-submit handle-submit}
          [:div (use-style s-input)
           [:div
            [:input (use-style s-text {:type "text"
                                       :id "answer"
                                       :name "answer"
                                       :value (values "answer")
                                       :autoFocus true
                                       :on-change handle-change
                                       :on-blur handle-blur
                                       :on-key-press (fn [e]
                                                       (when (and (not before-answer?) (= (.-key e) "Enter"))
                                                         (.preventDefault e)
                                                         (next-page-action set-values)))
                                       :read-only (not before-answer?)})]
            [:input {:type "hidden"
                     :id "uid"
                     :name "uid"
                     :value (values "uid")
                     :read-only true}]
            [:input {:type "hidden"
                     :id "correct-text"
                     :name "correct-text"
                     :value (values "correct-text")
                     :read-only true}]]
           [:div (use-style s-buttons-container)
            (if before-answer?
              [:span (use-style s-buttons-wrap)
               [:button (use-style sf/s-submit {:type "submit" :disabled submitting?}) (tr "OK")]]
              [:<>
               [JudgementMark (:judgement card)]
               [:span (tr (:judgement card))]
               [:span (use-style s-buttons-wrap)
                [Button
                 {:kind "secondary"
                  :href "#"
                  :on-click (fn [e]
                              (.preventDefault e)
                              (next-page-action set-values))}
                 (tr "Next")]]])]]])]]]))
