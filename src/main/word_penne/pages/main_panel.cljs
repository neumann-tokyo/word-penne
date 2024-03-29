(ns word-penne.pages.main-panel
  (:require [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [word-penne.views :as v]
            [word-penne.subs :as subs]
            [word-penne.style.vars :refer [color phone-width z-indexs]]
            [word-penne.components.header :refer [Header]]
            [word-penne.components.navigation :refer [Navigation]]
            [word-penne.components.word-card-add-button :refer [WordCardAddButton]]
            [word-penne.components.signouted-header :refer [SignoutedHeader]]
            [word-penne.pages.home]
            [word-penne.pages.cards]
            [word-penne.pages.auth]
            [word-penne.pages.tags]
            [word-penne.pages.user]))

(def s-main-panel
  {:background-color (:main-background color)
   :color (:main-text color)
   :height "100vh"})
(def s-main-container
  {:display "flex"})
(def s-main
  {:width "100%"
   :flex 1})
(def s-word-card-add-button
  {:display "none"
   ::stylefy/media {phone-width {:display "inline-block"
                                 :position "fixed"
                                 :bottom 0
                                 :right 0
                                 :margin "1rem"
                                 :z-index (:bottom-word-card-add-button z-indexs)}}})

(defn main-panel []
  (let [current-route (:handler @(re-frame/subscribe [::subs/current-route]))]
    [:div (use-style s-main-panel)
     (if (and @(re-frame/subscribe [::subs/current-user])
              (not= current-route :word-penne.pages.auth/signin))
       ;; when user signed in
       (if (= current-route :word-penne.pages.cards/quiz)
         ;; NOTE quizのときは Header などを外す
         [:<>
          [:div (use-style s-main-container)
           [:main (use-style s-main {:id "main"}) [v/view @(re-frame/subscribe [::subs/current-route])]]]]

         ;; NOTE 通常のページの処理
         [:<>
          [Header]
          [:div (use-style s-main-container)
           [Navigation]
           [:main (use-style s-main {:id "main"}) [v/view @(re-frame/subscribe [::subs/current-route])]]]
          [:div (use-style s-word-card-add-button)
           [WordCardAddButton]]])
       [:<> ;; when user didn't sign in
        [SignoutedHeader]
        [:div (use-style s-main-container)
         [:main (use-style s-main {:id "main"}) [v/view {:handler :word-penne.pages.auth/signin}]]]])]))
