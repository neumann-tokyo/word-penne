(ns word-penne.components.word-card-large
  (:require [clojure.string :as str]
            [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.style.vars :refer [color phone-width]]
            [word-penne.style.share :as share]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]))

(def s-container
  {:width "80%"
   :margin "0 auto"
   ::stylefy/media {phone-width {:width "100%"}}})
(def s-box-shadow
  {:box-shadow (str "0 2px 4px 0 " (:assort-border color))})
(def s-card
  (merge share/m-card
         {:text-align "center"}))
(def s-front
  {:font-size "2rem"
   :font-weight "bold"})
(def s-flip-card-buttons
  {:text-align "right"
   :font-size ".8rem"
   :display "flex"
   :flex-direction "row"
   :justify-content "space-between"
   :align-items "center"})
(def s-flip-card-button
  {:color (:main-text color)})

(defn WordCardLarge [options card]
  @(re-frame/subscribe [::subs/locale])
  [:div (use-style s-container)
   [:div (use-style (if (:focus options)
                      (merge s-card s-box-shadow)
                      s-card))
    [:div (use-style s-front) (:front card)]
    [:div (:back card)]
    (when (:comment card)
      [:div {:dangerouslySetInnerHTML
             {:__html (-> (:comment card)
                          (str/replace #"\n" "<br>"))}}])
    [:div (use-style s-flip-card-buttons)
     [:div
      [:a (use-style s-flip-card-button {:href "#"
                                         :on-click (fn [e]
                                                     (.preventDefault e)
                                                     (re-frame/dispatch [::events/navigate :word-penne.pages.cards/edit {:id (:uid card)}]))
                                         :title "edit"})
       [:span {:class "material-icons-outlined"} "edit"]]]
     [:div
      [:span (str (tr "Wrong") ": " (int (* (:wrongRate card) 100)) "%")]]
     [:div
      (let [title (if (:archive card) "unarchive" "archive")]
        [:a (use-style s-flip-card-button {:href "#"
                                           :on-click (fn [e]
                                                       (.preventDefault e)
                                                       (re-frame/dispatch [::events/archive-card (:uid card) (not (:archive card))]))
                                           :title title})
         [:span {:class "material-icons-outlined"} title]])
      [:a (use-style s-flip-card-button {:href "#"
                                         :title "delete"
                                         :on-click (fn [e]
                                                     (.preventDefault e)
                                                     (re-frame/dispatch [::events/set-selected-card card])
                                                     (re-frame/dispatch [::events/show-confirmation-modal]))})
       [:span {:class "material-icons-outlined"} "delete"]]]]]])
