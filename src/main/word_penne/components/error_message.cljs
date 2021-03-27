(ns word-penne.components.error-message
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.style.form :as sf]
            [word-penne.i18n :refer [tr]]))

(defn ErrorMessange [touched errors target]
  @(re-frame/subscribe [::subs/locale])
  (when (touched target)
    (when-let [message (first (get errors (list target)))]
      [:div (use-style sf/s-error-message) (tr message)])))
