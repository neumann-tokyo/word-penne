(ns word-penne.fx.quiz
  (:require [reagent.core :as r]
            [word-penne.firebase.firestore :refer [firestore] :as fs]))

(defn get-cards [snapshot face]
  (let [cards (r/atom [])]
    (.forEach
     snapshot
     (fn [doc]
       (let [card (js->clj (.data doc) :keywordize-keys true)
             front-quiz {:uid (.-id doc) :front (:front card) :back (:back card) :comment (:comment card)}
             back-quiz {:uid (.-id doc) :front (:back card) :back (:front card) :comment (:comment card)}]
         (swap! cards
                into
                (case face
                  "Front" [front-quiz]
                  "Back" [back-quiz]
                  ;; "Both" is default 
                  [front-quiz back-quiz])))))
    @cards))

(defn- xxx [fns {:keys [user-uid item-count]}]
  (-> (firestore)
      (.collection (str "users/" user-uid "/cards"))
      ((fn [f] ((apply comp (reverse fns)) f)))
      (.where "archive" "==" false)
      (.limit item-count)
      (.get)))

;; promise を返すので <p! で処理すること
(defmulti fetch-cards
  (fn [{:keys [kind]}]
    kind))

(defmethod fetch-cards "Latest" [{:keys [user-uid item-count]}]
  (-> (firestore)
      (.collection (str "users/" user-uid "/cards"))
      (.orderBy "updatedAt" "desc") ;; TODO もう少し random な要素を出せるなら出したい
      (.where "archive" "==" false)
      (.limit item-count)
      (.get)))

(defmethod fetch-cards "High wrong rate" [{:keys [user-uid item-count]}]
  (-> (firestore)
      (.collection (str "users/" user-uid "/cards"))
      (.orderBy "wrongRate" "desc") ;; TODO もう少し random な要素を出せるなら出したい
      (.where "archive" "==" false)
      (.limit item-count)
      (.get)))

(defmethod fetch-cards "Random" [{:keys [user-uid item-count rand-range]}]
  (let [start-at (rand-int rand-range)]
    (-> (firestore)
        (.collection (str "users/" user-uid "/cards"))
        (.orderBy "random")
        (.orderBy "wrongRate") ;; TODO これ不要かもしれない
        (.where "random" ">=" start-at)
        (.where "archive" "==" false)
        (.limit item-count)
        (.get))))
