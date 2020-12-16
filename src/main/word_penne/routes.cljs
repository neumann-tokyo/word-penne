(ns word-penne.routes
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :word-penne.pages.home/home
        "new-card" :word-penne.pages.new-card/new-card}])
; "list" :word-penne.pages.cards/list
; "create" :word-penne.pages.cards/create
; [[#"\d+" :id] "/edit"] :word-penne.pages.cards/edit

(defn navigate [view]
  (accountant/navigate! (bidi/path-for routes view)))
