(ns word-penne.components.quiz-slider
  (:require [re-frame.core :as re-frame]
            [fork.reagent :as fork]
            [stylefy.core :as stylefy :refer [use-style]]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide ButtonNext ButtonBack]]
            [word-penne.components.quiz-slide :refer [QuizSlide]]
            [word-penne.components.quiz-result :refer [QuizResult]]
            [word-penne.subs :as subs]))

(def s-carousel-provider
  {::stylefy/manual ["~" [:* {:outline "none !important"}]]})
(def s-slider
  {:height "80vh"})
(def s-slide
  {})

;; TODO 正誤率を単語に反映したい
;; TODO Enterを押したらOKをクリックしたのと同じ扱いにしたい
(defn QuizSlider []
  (let [slide-size (inc (count @(re-frame/subscribe [::subs/quiz-cards])))]
    [:> CarouselProvider (use-style s-carousel-provider {:naturalSlideWidth "100"
                                                         :naturalSlideHeight "100"
                                                         :totalSlides slide-size
                                                         :touchEnabled false
                                                         :dragEnabled false})
     ;; TODO あとで消す  
     [:div
      [:> ButtonBack "back"]
      [:> ButtonNext "next"]]

     [fork/form {:path [:form]
                 :prevent-default? true
                 :clean-on-unmount? true}
      (fn [{:keys [values
                   errors
                   touched
                   form-id
                   handle-change
                   handle-blur
                   submitting?
                   handle-submit] :as f-props}]
        [:form {:id form-id :on-submit handle-submit}
         [:> Slider (use-style s-slider)
          (doall (map-indexed
                  (fn [index card]
                    ^{:key index} [:> Slide (use-style s-slide {:index index}) [QuizSlide
                                                                                f-props
                                                                                {:front (:front card)
                                                                                 :back (:back card)
                                                                                 :index index}]])
                  @(re-frame/subscribe [::subs/quiz-cards])))
          [:> Slide (use-style s-slide {:index slide-size}) [QuizResult f-props {:cards @(re-frame/subscribe [::subs/quiz-cards])}]]]])]]))
