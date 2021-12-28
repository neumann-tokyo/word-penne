(ns word-penne.routes
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :word-penne.pages.home/home
        "auth/" {"" :word-penne.pages.auth/signin}
        "user/" {"edit" :word-penne.pages.user/edit}
        "cards/" {"new" :word-penne.pages.cards/new
                  "quiz" :word-penne.pages.cards/quiz
                  [:id] :word-penne.pages.cards/show
                  [:id "/edit"] :word-penne.pages.cards/edit}
        "tags/" {"" :word-penne.pages.tags/index}}])

(defn navigate
  ([view] (navigate view {}))
  ([view params]
   (accountant/navigate! (apply bidi/path-for routes view (apply concat params)))))
