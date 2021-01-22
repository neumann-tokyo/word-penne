(ns word-penne.pages.tags
  (:require [re-frame.core :as re-frame]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.views :as v]
            [word-penne.components.tags-form :refer [TagsForm]]))

(defmethod v/view ::index [_]
  [:div
   [TagsForm {:initial-values {"tags" (mapv (fn [t] {"name" t "beforeName" t}) @(re-frame/subscribe [::subs/tags]))}
              :on-submit #(re-frame/dispatch [::events/update-tags %])}]])
