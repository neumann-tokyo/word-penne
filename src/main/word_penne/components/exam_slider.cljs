(ns word-penne.components.exam-slider
  (:require [stylefy.core :as stylefy :refer [use-style]]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide ButtonNext]]
            [word-penne.components.exam-slide :refer [ExamSlide]]))

(def s-carousel-provider
  {::stylefy/manual ["~" [:* {:outline "none !important"}]]})
(def s-slider
  {:height "70vh"})
(def s-slide
  {})

(defn ExamSlider []
  [:> CarouselProvider (use-style s-carousel-provider {:naturalSlideWidth "100"
                                                       :naturalSlideHeight "100"
                                                       :totalSlides "3"
                                                       :touchEnabled false
                                                       :dragEnabled false})
   [:> Slider (use-style s-slider)
    [:> Slide (use-style s-slide {:index "0"}) [ExamSlide {:front-text "make"
                                                           :back-text "作る"}]]
    [:> Slide (use-style s-slide {:index "1"}) [ExamSlide]]
    [:> Slide (use-style s-slide {:index "2"}) [ExamSlide]]]
   [:> ButtonNext "Next"]])
