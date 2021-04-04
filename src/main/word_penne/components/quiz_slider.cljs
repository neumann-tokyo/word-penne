(ns word-penne.components.quiz-slider
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide ButtonNext]]
            [word-penne.components.quiz-slide :refer [QuizSlide]]
            [word-penne.subs :as subs]))

(def s-carousel-provider
  {::stylefy/manual ["~" [:* {:outline "none !important"}]]})
(def s-slider
  {:height "70vh"})
(def s-slide
  {})

(defn QuizSlider []
  @(re-frame/subscribe [::subs/locale])

  [:> CarouselProvider (use-style s-carousel-provider {:naturalSlideWidth "100"
                                                       :naturalSlideHeight "100"
                                                       :totalSlides (count @(re-frame/subscribe [::subs/quiz-cards]))
                                                       :touchEnabled false
                                                       :dragEnabled false})
   ;; TODO テストに回答したら次の問題に行くボタンを表示する
   ;; QuizSlide内にはロジックを入れないほうが良さそう
   [:> Slider (use-style s-slider)
     (doall (map-indexed
         (fn [index card]
           ^{:key index} [:> Slide (use-style s-slide {:index index}) [QuizSlide {:front (:front card)
                                                                    :back (:back card)
                                                                    :index index}]])
         @(re-frame/subscribe [::subs/quiz-cards])))]
  　;; TODO あとで消す
   [:> ButtonNext "Next"]])
