(ns word-penne.fx
  (:require [re-frame.core :as re-frame]
            [word-penne.firebase.firestore :refer [firestore]]
            [word-penne.firebase.auth :as firebase-auth]
            [word-penne.routes :as routes]))

(re-frame/reg-fx
 ::navigate
 (fn [{:keys [view params]}]
   (routes/navigate view params)))

(re-frame/reg-fx
 ::firabase-signout
 (fn [{:keys [on-success on-failure]}]
   (-> (firebase-auth/auth)
       (.signOut)
       (.then on-success)
       (.catch on-failure))))

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

(re-frame/reg-fx
 ::firebase-load-card-by-uid
 (fn [{:keys [user-uid card-uid on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.doc card-uid)
         (.get)
         (.then (fn [doc]
                  (when (.-exists doc)
                    (re-frame/dispatch (on-success (conj {:uid card-uid} (js->clj (.data doc) :keywordize-keys true)))))))))))

(re-frame/reg-fx
 ::firebase-update-card-by-uid
 (fn [{:keys [user-uid card-uid values on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.doc card-uid)
         (.set (clj->js values))
         (.then (fn [_] (re-frame/dispatch (on-success))))))))
