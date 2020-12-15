(ns word-penne.views)

(defmulti view :handler)

(defmethod view :default [_]
  [:div "404 Not Found"])

; (defmethod view ::list [_]
;   [:div "Todo List"
;    [:a {:href (path-for routes :word-penne.views/home)} "top"]])

; (defmethod view ::create [_]
;   [:div "Create New Todo"])

; (defmethod view ::edit [{:keys [route-params]}]
;   [:div (str "Edit Todo " (:id route-params))])
