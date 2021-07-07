(ns word-penne.components.word-card
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [clojure.string :as str]
            [word-penne.events :as events]
            [word-penne.style.vars :refer [color layout-vars phone-width]]
            [word-penne.style.share :as share]
            [word-penne.components.tag-badges :refer [TagBadges]]
            [word-penne.components.speech-mark :refer [SpeechMark]]
            [word-penne.subs :as subs]
            [word-penne.i18n :refer [tr]]))

(def s-card
  {:display "inline-block"})
(def s-flip-card
  {:background-color "transparent"
   :border "none"
   :perspective "1000px"
   :width (:word-card-width layout-vars)
   :height "max-content"
   :margin 0
   :padding 0
   :tabindex 0
   ::stylefy/mode {:focus {:outline "none"}}
   ::stylefy/media {phone-width {:width "85vw"}}})
(def s-flip-card-inner
  {:display "grid"
   :width (:word-card-width layout-vars)
   :grid-template-columns "1fr"
   :transition "transform 0.6s"
   :transform-style "preserve-3d"
   :border "none"
   ::stylefy/media {phone-width {:width "85vw"}}})

(def m-flip-card
  (merge share/m-card
         {:width (:word-card-width layout-vars)
          :-webkit-backface-visibility "hidden"
          :backface-visibility "hidden"
          :grid-column 1
          :grid-row 1
          ::stylefy/mode {:hover {:box-shadow (str "0 2px 4px 0 " (:assort-border color))}}
          ::stylefy/media {phone-width {:width "85vw"}}}))
(def s-flip-card-front
  (merge m-flip-card {:background-color (:main-background color)
                      :display "flex"
                      :flex-direction "column"
                      :justify-content "center"
                      :align-items "center"}))
(def s-flip-card-back
  (merge m-flip-card {:background-color (:assort-background color)
                      :transform "rotateY(180deg)"
                      :display "flex"
                      :flex-direction "column"}))
(def s-flip-card-front-title
  {:font-size "2rem"
   :font-weight "bold"
   :text-align "center"})
(def s-tags-container
  {:text-align "center"})
(def s-flip-card-back-title-container
  {:flex "1"})
(def s-flip-card-back-title
  {:font-size "2rem"
   :font-weight "bold"
   :text-align "center"
   :outline "none"
   :margin-top ".5rem"})
(def s-flip-card-buttons
  {:text-align "right"
   :font-size ".8rem"
   :display "flex"
   :flex-direction "row"
   :justify-content "space-between"
   :align-items "center"})
(def s-flip-card-button
  {:color (:main-text color)})

(defn- card-color [{:keys [quizCount wrongRate]}]
  (cond
    (= quizCount 0) (:main-text color)
    (< 0.60 wrongRate) (:bad-card-text color)
    (< 0.30 wrongRate) (:weak-card-text color)
    :else (:good-card-text color)))

(defn rotate-card [uid]
  (let [reverse-cards? (if @(re-frame/subscribe [::subs/reverse-cards]) 1 0)
        clicked-card? (if (= uid @(re-frame/subscribe [::subs/clicked-card-uid])) 1 0)]
    (if (= (bit-xor reverse-cards? clicked-card?) 1)
      "rotateY(180deg)"
      nil)))

;; https://www.w3schools.com/howto/howto_css_flip_card.asp
;; https://www.w3schools.com/tags/tag_details.asp
(defn WordCard [attrs]
  [:div (use-style s-card)
   [:button (use-style s-flip-card {:on-click (fn [e]
                                                (.preventDefault e)
                                                (re-frame/dispatch
                                                 [::events/set-clicked-card-uid
                                                  (if (= (:uid attrs) @(re-frame/subscribe [::subs/clicked-card-uid]))
                                                    nil
                                                    (:uid attrs))]))})
    [:div (merge (use-style s-flip-card-inner)
                 {:style {:transform (rotate-card (:uid attrs))}})
     [:div (use-style s-flip-card-front)
      [:div (merge (use-style s-flip-card-front-title)
                   {:style {:color (card-color attrs)}})
       (:front attrs)
       [SpeechMark (:front attrs)]]
      [:div (use-style s-tags-container)
       [TagBadges (:tags attrs)]]]
     [:div (use-style s-flip-card-back)
      [:div (use-style s-flip-card-back-title-container)
       [:div (use-style s-flip-card-back-title)
        (:back attrs)
        [SpeechMark (:back attrs)]]
       (when (:comment attrs)
         [:p {:dangerouslySetInnerHTML
              {:__html (str/replace (:comment attrs) #"\n" "<br>")}}])]
      [:div (use-style s-tags-container)
       [TagBadges (:tags attrs)]]
      [:div (use-style s-flip-card-buttons)
       [:div
        [:a (use-style s-flip-card-button {:href "#"
                                           :on-click (fn [e]
                                                       (.preventDefault e)
                                                       (re-frame/dispatch [::events/lock-card (:uid attrs) (not (:lock attrs))]))
                                           :title "pin"})
         [:span {:class "material-icons-outlined"} "push_pin"]]
        [:a (use-style s-flip-card-button {:href "#"
                                           :on-click (fn [e]
                                                       (.preventDefault e)
                                                       (re-frame/dispatch [::events/navigate :word-penne.pages.cards/edit {:id (:uid attrs)}]))
                                           :title "edit"})
         [:span {:class "material-icons-outlined"} "edit"]]]
       [:div
        [:span (str (tr "Wrong") ": " (int (* (:wrongRate attrs) 100)) "%")]]
       [:div
        (let [title (if (:archive attrs) "unarchive" "archive")]
          [:a (use-style s-flip-card-button {:href "#"
                                             :on-click (fn [e]
                                                         (.preventDefault e)
                                                         (re-frame/dispatch [::events/archive-card (:uid attrs) (not (:archive attrs))]))
                                             :title title})
           [:span {:class "material-icons-outlined"} title]])
        [:a (use-style s-flip-card-button {:href "#"
                                           :title "delete"
                                           :on-click (fn [e]
                                                       (.preventDefault e)
                                                       (re-frame/dispatch [::events/set-selected-card attrs])
                                                       (re-frame/dispatch [::events/show-confirmation-modal]))})
         [:span {:class "material-icons-outlined"} "delete"]]]]]]]])
