(ns word-penne.validator
  (:require [malli.core :as m]
            [malli.error :as me]
            [malli.transform :as mt]
            [vlad.core :as vlad]))

;; https://github.com/stevebuik/fork-malli-ideas/blob/master/src/core.cljs

(def form-transforms (mt/transformer
                      (mt/key-transformer {:decode keyword}) ; keys back to keywords
                      mt/string-transformer                  ; and strings from inputs back to integers
                      ))

;; TODO おそらくエラーの返し方が悪くて fork でエラーが出せてない
(defn validator-for-humans
  "HOF returning a Fork compatible validation fn from a schema."
  [schema]
  ; pre-compile schema for best performance
  (let [explain (m/explainer schema)]
    (fn [v]
      (-> (m/decode schema v form-transforms)
           ; validate the values map
          (explain)
           ; return a map of error messages suitable for human UIs
          (me/humanize)))))

;; {"name" ["Name must be under 15 characters long. "], "password" [" Password is required. "]}}
;; {'(" name ") [" Name must be under 15 characters long."], '("password") ["Password is required."]}}

(def validation
  (vlad/join (vlad/attr ["name"]
                        (vlad/chain
                         (vlad/present)
                         (vlad/length-in 3 15)))
             (vlad/attr ["password"]
                        (vlad/chain
                         (vlad/present)
                         (vlad/length-over 7)))))
