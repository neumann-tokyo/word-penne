(ns word-penne.components.quiz-slide
  (:require [re-frame.core :as re-frame]
            [clojure.string :as str]
            [stylefy.core :as stylefy :refer [use-style]]
            [bidi.bidi :refer [path-for]]
            ["pure-react-carousel" :refer [ButtonNext]]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.events :as events]
            [word-penne.routes :refer [routes]]
            [word-penne.style.share :as share]
            [word-penne.components.button :refer [Button]]
            [word-penne.components.judgement-mark :refer [JudgementMark]]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]))

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

(defn QuizSlide [{:keys [values handle-change handle-blur set-values]} attrs]
  @(re-frame/subscribe [::subs/locale])
  [:div
   [:div (use-style s-header)
    [:a (use-style s-cancel-link {:href "#"
                                  :title "cancel"
                                  :on-click (fn [e]
                                              (.preventDefault e)
                                              (re-frame/dispatch [::events/show-confirmation-modal]))})
     [:span {:class "material-icons-outlined"} "cancel"]]]

   (let [index (:index attrs)
         answer-id (str "answer-" index)
         judgement-id (str "judgement-" index)
         card-id (str "uid-" index)
         button-id (str "quiz-slide-button-" index)]
     [:div (use-style s-container)
      [:div (use-style s-card)
       [:div (use-style s-card-inner (if (str/blank? (values judgement-id)) {} {:class "turned-flip"}))
        [:div (use-style s-card-front) (:front attrs)]
        [:div (use-style s-card-back) (:back attrs)]]]
      [:div (use-style s-input)
       [:div
        [:input (use-style s-text {:type "text"
                                   :id answer-id
                                   :name answer-id
                                   :value (values answer-id)
                                   :on-change handle-change
                                   :on-blur handle-blur
                                   :on-key-press (fn [e]
                                                   (when (= (.-key e) "Enter")
                                                     (.preventDefault e)
                                                    ;; NOTE Nextボタンを押さないと次のページに行くアクションができないので仕方なくDOM操作をしている
                                                    ;; TODO on-key-press は deplicated ?
                                                    ;; TODO dom操作よりrefを使うほうが無難 ?
                                                     (.click (js/document.getElementById button-id))))
                                   :on-key-down (fn [e]
                                                  (when (= (.-key e) "Tab")
                                                    (.preventDefault e)))
                                   :read-only (not (str/blank? (values judgement-id)))})]
        [:input {:type "hidden"
                 :id judgement-id
                 :name judgement-id
                 :value (values judgement-id)
                 :on-change handle-change
                 :on-blur handle-blur}]]
       [:div (use-style s-buttons-container)
        (if (str/blank? (values judgement-id))
          [:span (use-style s-buttons-wrap)
           [Button {:id button-id
                    :href "#"
                    :kind "secondary"
                    :on-click (fn [e]
                                (.preventDefault e)
                                (let [judgement (cond
                                                  (nil? (values answer-id)) "Wrong"
                                                  (= (str/trim (values answer-id)) (:back attrs)) "Correct"
                                                  :else "Wrong")]
                                  (set-values {card-id (:uid attrs)})
                                  (set-values {judgement-id judgement})))} (tr "OK")]]
          [:<>
           [JudgementMark (values judgement-id)]
           [:span (tr (values judgement-id))]
           [:span (use-style s-buttons-wrap)
            [:> ButtonNext (use-style share/m-button {:id button-id}) (tr "Next")]]])]]])])
