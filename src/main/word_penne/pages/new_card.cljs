(ns word-penne.pages.new-card
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]))

(defmethod v/view ::new-card [_]
  [:div "new card"])
