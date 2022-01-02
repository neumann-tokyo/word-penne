(ns word-penne.fx.quiz
  (:require [reagent.core :as r]
            [word-penne.firebase.firestore :refer [firestore] :as fs]))

(defn- make-faces [face card]
  (let [front-quiz {:uid (:uid card) :front (:front card) :back (:back card) :comment (:comment card)}
        back-quiz {:uid (:uid card) :front (:back card) :back (:front card) :comment (:comment card)}]
    (case face
      "Front" [front-quiz]
      "Back" [back-quiz]
      ;; "Both" is default 
      [front-quiz back-quiz])))

(defn get-cards [snapshot {:keys [face item-count]}]
  (let [cards (r/atom [])]
    (.forEach
     snapshot
     (fn [doc]
       (swap! cards conj (conj (js->clj (.data doc) :keywordize-keys true)
                               {:uid (.-id doc)}))))
    (->> @cards
         shuffle
         (take item-count)
         (mapcat (partial make-faces face)))))

(defn- with-fetch-cards [fns {:keys [user-uid item-count tags]}]
  (-> (firestore)
      (.collection (str "users/" user-uid "/cards"))
      ((apply comp (reverse fns)))
      (.where "archive" "==" false)
      ((fn [f]
         (if (seq tags)
           (.where f "tags" "array-contains-any" #js[tags])
           f)))
      (.limit item-count)
      (.get)))

;; promise を返すので <p! で処理すること
(defmulti fetch-cards
  (fn [{:keys [kind]}] kind))

;; 50件とっておいて後で get-cards で item-count の件数に絞り込む
(defmethod fetch-cards "Latest" [params]
  (with-fetch-cards
    [#(.orderBy % "updatedAt" "desc")]
    (assoc params :item-count 50)))

(defmethod fetch-cards "High wrong rate" [params]
  (with-fetch-cards
    [#(.orderBy % "wrongRate" "desc")]
    params))

(defmethod fetch-cards "Random" [{:keys [rand-range] :as params}]
  (let [start-at (rand-int rand-range)]
    (with-fetch-cards
      [#(.where % "random" ">=" start-at)
       #(.orderBy % "random")]
      params)))
