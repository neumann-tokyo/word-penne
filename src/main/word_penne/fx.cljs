(ns word-penne.fx
  (:require [re-frame.core :as re-frame]
            [word-penne.firebase.firestore :refer [firestore]]
            [word-penne.routes :as routes]))

(re-frame/reg-fx
 ::navigate
 (fn [{:keys [view params]}]
   (routes/navigate view params)))

(re-frame/reg-fx
 ::firebase-load-cards
 (fn [{:keys [user-uid on-success]}] ; TODO I want to pass a sort order
   (when user-uid
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.get)
         (.then
          (fn [snapshot]
            (let [result (atom [])]
              (.forEach snapshot
                        (fn [doc]
                          (swap! result conj
                                 (conj {:uid (.-id doc)}
                                       (js->clj (.data doc) :keywordize-keys true)))))
              (re-frame/dispatch (on-success @result)))))))))

;; TODO core.spec
(re-frame/reg-fx
 ::firebase-create-card
 (fn [{:keys [user-uid values on-success]}]
   (-> (firestore)
       (.collection (str "users/" user-uid "/cards"))
       (.add (clj->js values))
       (.then (fn [_]
                (re-frame/dispatch (on-success)))))))
