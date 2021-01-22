(ns word-penne.components.tags-input
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.style.form :as sf]))

(def ^:private s-tag-input-container
  {:display "flex"
   :align-items "center"
   :width "100%"})
(def ^:private s-tag-button
  {:background "none"
   :border "none"
   :outline "none"
   :margin 0
   :padding 0})
(def ^:private s-tag-add-button-container
  {:text-align "right"
   :margin-bottom "1rem"})

;; see https://github.com/luciodale/fork#field-array
(defn TagsInput
  [_props
   {:fieldarray/keys [fields
                      insert
                      remove
                      handle-change
                      handle-blur]}]
  [:<>
   (doall (map-indexed
           (fn [idx field]
             ^{:key idx}
             [:div (use-style s-tag-input-container)
              [:input {:type "hidden"
                       :name "beforeName"
                       :id (str "before-tag-name-" idx)
                       :value (get field "beforeName")}]
              [:input
               (use-style sf/s-text
                          {:type "text"
                           :name "name"
                           :id (str "tag-name-" idx)
                           :list "tag-list"
                           :data-testid (str "tag-name-" idx)
                           :value (get field "name")
                           :on-change #(handle-change % idx)
                           :on-blur #(handle-blur % idx)})]
              [:button (use-style s-tag-button
                                  {:type "button"
                                   :on-click #(when (> (count fields) 1) (remove idx))})
               [:span {:class "material-icons-outlined"} "remove_circle"]]])
           fields))
   [:div (use-style s-tag-add-button-container)
    [:button (use-style s-tag-button
                        {:type "button"
                         :on-click #(insert {"name" "" "beforeName" ""})})
     [:span {:class "material-icons-outlined"} "add_circle"]]]])
