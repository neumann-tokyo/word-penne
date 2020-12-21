(ns word-penne.components.quiz-slider
  (:require [stylefy.core :as stylefy :refer [use-style]]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide ButtonNext]]
            [word-penne.components.quiz-slide :refer [QuizSlide]]))

(def s-carousel-provider
  {::stylefy/manual ["~" [:* {:outline "none !important"}]]})
(def s-slider
  {:height "70vh"})
(def s-slide
  {})

(defn QuizSlider []
  [:> CarouselProvider (use-style s-carousel-provider {:naturalSlideWidth "100"
                                                       :naturalSlideHeight "100"
                                                       :totalSlides "3"
                                                       :touchEnabled false
                                                       :dragEnabled false})
   [:> Slider (use-style s-slider)
    [:> Slide (use-style s-slide {:index "0"})
     [QuizSlide {:front-text "make"
                 :back-text "作る"
                 :index "0"}]
     [:> ButtonNext "Next"]]
    [:> Slide (use-style s-slide {:index "1"}) [QuizSlide {:index "1"}]]
    [:> Slide (use-style s-slide {:index "2"}) [QuizSlide {:index "2"}]]]
   [:> ButtonNext "Next"]])
