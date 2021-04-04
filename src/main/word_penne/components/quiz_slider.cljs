(ns word-penne.components.quiz-slider
  (:require [re-frame.core :as re-frame]
            [fork.reagent :as fork]
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
  [:> CarouselProvider (use-style s-carousel-provider {:naturalSlideWidth "100"
                                                       :naturalSlideHeight "100"
                                                       :totalSlides (count @(re-frame/subscribe [::subs/quiz-cards]))
                                                       :touchEnabled false
                                                       :dragEnabled false})
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
                @(re-frame/subscribe [::subs/quiz-cards])))]])]
  　;; TODO あとで消す
   [:> ButtonNext "Next"]])
