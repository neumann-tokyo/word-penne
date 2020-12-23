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
 (fn [{:keys [user-uid]}] ; TODO I want to pass a sort order
   (-> (firestore)
       (.collection (str "users/" user-uid "/cards"))
       (.get)
       (.then
        (fn [snapshot]
          (let [result (atom [])]
            (.forEach snapshot
                      (fn [doc]
                        (swap! result conj
                               (conj {:id (.-id doc)}
                                     (js->clj (.data doc) :keywordize-keys true)))))
            (re-frame/dispatch [::set-cards @result])))))))
