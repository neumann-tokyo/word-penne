(ns word-penne.components.word-cards-wrap
  (:require ["react-infinite-scroll-component" :as InfiniteScroll]
            [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.subs :as subs]
            [word-penne.events :as events]
            [word-penne.components.word-card :refer [WordCard]]
            [word-penne.style.vars :refer [color layout-vars phone-width]]))

(def s-cards-wrap
  {:width "100%"
   :height "100%"
   :margin-bottom "2rem"
   :display "flex"
   :flex-wrap "wrap"
   :justify-content "space-around"
   :align-items "center"
   :background (:main-background color)
   ::stylefy/media {phone-width {:flex-direction "column"}}})
(def s-card-item
  {:break-inside "avoid"
   :margin ".5rem"
   :width "100%"
   :max-width (:word-card-width layout-vars)
   :display "inline-block"})

(defn- word-card-container [target]
  (when-let [cards (seq @(re-frame/subscribe target))]
    [:div (use-style s-cards-wrap
                     {:on-click (fn [e]
                                  (.preventDefault e)
                                  (re-frame/dispatch
                                   [::events/set-clicked-card-uid nil]))})
     (doall (for [card cards]
              [:div (use-style s-card-item {:key (:uid card)})
               [WordCard card]]))]))

(defn WordCardsWrap []
  [:> InfiniteScroll
   {:dataLength (count @(re-frame/subscribe [::subs/unlocked-cards]))
    :next #(re-frame/dispatch [::events/fetch-cards {:last-visible (last @(re-frame/subscribe [::subs/cards]))}])
    :hasMore true
    :style #js {:overflowY "hidden"}
    :loader "Loading..."}
   (word-card-container [::subs/locked-cards])
   (word-card-container [::subs/unlocked-cards])])
