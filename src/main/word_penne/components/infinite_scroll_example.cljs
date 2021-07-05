(ns word-penne.components.infinite-scroll-example
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [reagent.core :as r]
            ["react-infinite-scroll-component" :refer (InfiniteScroll)]))

(def s-container
  {:margin-bottom "2rem"})
(def s-foo
  {:height 30
   :border "1px solid green"
   :margin 6
   :padding 8})

(defn InfiniteScrollExample []
  (let [items (r/atom (take 10 (repeat 0)))
        fetchMoreData (fn []
                        (js/setTimeout
                         (fn []
                           (swap! items concat (take 10 (repeat 0))))
                         1500))]
    [:p (use-style s-container)
     [:> InfiniteScroll
      {:dataLength (count @items)
       :next fetchMoreData
       :hasMore true
       :loader "Loading..."}
      (map-indexed
       (fn [index item]
         [:div (use-style s-foo {:key index})
          (str "div - #" index)])
       @items)]]))
