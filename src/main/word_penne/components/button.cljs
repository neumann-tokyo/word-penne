(ns word-penne.components.button
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def s-button
  {:margin "0 .5rem"
   :background "#dddddd"
   :color "black"
   :border "none"
   :padding ".5rem 1rem"
   :text-align "center"
   :text-decoration "none"
   :display "inline-block"
   :border-radius "10px"})

; TODO この辺見ていい感じのデザインにする
; https://github.com/Jarzka/stylefy#passing-styles-to-components
(defn Button [params text]
  [:a (use-style s-button params) text])
