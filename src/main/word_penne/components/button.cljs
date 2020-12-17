(ns word-penne.components.button
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.share :as share]))

(def s-button
  share/m-button)

; TODO この辺見ていい感じのデザインにする
; https://github.com/Jarzka/stylefy#passing-styles-to-components
(defn Button [params text]
  [:a (use-style s-button params) text])
