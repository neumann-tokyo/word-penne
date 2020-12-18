(ns word-penne.routes
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :word-penne.pages.home/home
        "cards/" {"new" :word-penne.pages.cards/new
                  [:id "/edit"] :word-penne.pages.cards/edit
                  "test" :word-penne.pages.cards/test}}])
; "list" :word-penne.pages.cards/list
; "create" :word-penne.pages.cards/create
; [[#"\d+" :id] "/edit"] :word-penne.pages.cards/edit

(defn navigate [view]
  (accountant/navigate! (bidi/path-for routes view)))
