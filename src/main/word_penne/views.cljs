(ns word-penne.views)

(defmulti view :handler)

(defmethod view :default [_]
  [:div "404 Not Found"])
