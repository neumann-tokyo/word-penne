(ns word-penne.pages.auth
  (:require [word-penne.views :as v]))

(defmethod v/view ::signin [_]
  [:div "signin"])
