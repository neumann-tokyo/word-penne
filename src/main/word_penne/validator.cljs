(ns word-penne.validator
  (:require [malli.core :as m]
            [malli.error :as me]
            [malli.transform :as mt]))

;; https://github.com/stevebuik/fork-malli-ideas/blob/master/src/core.cljs

(def form-transforms (mt/transformer
                      (mt/key-transformer {:decode keyword}) ; keys back to keywords
                      mt/string-transformer                  ; and strings from inputs back to integers
                      ))

(defn validator-for-humans
  "returning a Fork compatible validation fn from a schema."
  [schema]
  ; pre-compile schema for best performance
  (let [explain (m/explainer schema)]
    (fn [v]
      (let [errors (-> (m/decode schema v form-transforms)
                       (explain)
                       (me/humanize))]
        (reduce (fn [r [k v]] (conj r {(list (name k)) v})) {} errors)))))
