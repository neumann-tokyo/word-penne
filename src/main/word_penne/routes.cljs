(ns word-penne.routes
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :word-penne.views/home
        "list" :word-penne.views/list
        "create" :word-penne.views/create
        [[#"\d+" :id] "/edit"] :word-penne.views/edit}])

(defn navigate [view]
  (accountant/navigate! (bidi/path-for routes view)))
