(ns word-penne.components.word-card-large-wrap
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [re-frame.core :as re-frame]
            [word-penne.style.vars :refer [layout-vars color phone-width]]
            [word-penne.subs :as subs]
            [word-penne.events :as events]
            [word-penne.components.word-card-large :refer [WordCardLarge]]))

;; TODO 全体的にCSSグリッドにしたほうが簡単に揃うかも
(def s-container
  {:width "80%"
   :margin "0 auto"
   ::stylefy/media {phone-width {:width "100%"}}})
(def s-cards-wrap
  {:width "100%"
   :height "100%"
   :column-count "auto"
   :margin "2rem 0"
   :column-width (:word-card-width layout-vars)
   :background (:main-background color)})

(defn WordCardLargeWrap [card]
  @(re-frame/subscribe [::subs/locale])
  (let [search-words (if (:comment card)
                       (map second (re-seq #"#([^\W]*)" (:comment card)))
                       [])]
    (re-frame/dispatch [::events/set-relational-cards []])
    (prn "aaaaaaa")
    (prn (:comment card))
    (prn search-words)
    (doall (map #(re-frame/dispatch [::events/fetch-relational-cards {:search-word %}]) search-words))
    nil)

  [:div (use-style s-container)
   [WordCardLarge {:focus true} card]
   [:div (use-style s-cards-wrap)
    (doall (for [relational-card @(re-frame/subscribe [::subs/relational-cards])]
             [WordCardLarge {} relational-card]))]])