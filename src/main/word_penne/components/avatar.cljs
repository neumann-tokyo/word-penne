(ns word-penne.components.avatar
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def s-avatar
  {:vertical-align "middle"
   :width "2.5rem"
   :height "2.5rem"
   :border-radius "50%"})

(defn Avatar [attrs]
  [:img (use-style s-avatar attrs)])
