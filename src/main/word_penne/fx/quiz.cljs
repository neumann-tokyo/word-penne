(ns word-penne.fx.quiz
  (:require [reagent.core :as r]
            ["spacetime" :as spacetime]
            [word-penne.firebase.firestore :refer [firestore timestamp] :as fs]))

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

(defn- with-fetch-cards [fns {:keys [user-uid item-count]}]
  (-> (firestore)
      (.collection (str "users/" user-uid "/cards"))
      ((apply comp (reverse fns)))
      (.where "archive" "==" false)
      (.limit item-count)
      (.get)))

;; promise を返すので <p! で処理すること
(defmulti fetch-cards
  (fn [{:keys [kind]}]
    kind))

(defmethod fetch-cards "Latest" [params]
  ;; 1週間以内に更新があったデータからランダムに取得する
  (let [now (-> (timestamp) .toDate spacetime)
        min-start-date (-> now (.subtract 1 "week") .toNativeDate)
        rand-range (- (.toNativeDate now) min-start-date)
        start-int (rand-int rand-range)
        end-int (rand-int (- rand-range start-int))
        start-date (js/Date. (+ (.getTime min-start-date) start-int))
        end-date (js/Date. (+ (.getTime min-start-date) start-int end-int))]
    (with-fetch-cards
      [#(-> %
            (.where "updatedAt" ">=" start-date)
            (.where "updatedAt" "<=" end-date)
            (.orderBy "updatedAt"))]
      params)))

(defmethod fetch-cards "High wrong rate" [params]
  (with-fetch-cards
    [#(.orderBy % "wrongRate" "desc") ;; TODO もう少し random な要素を出せるなら出したい
     ]
    params))

(defmethod fetch-cards "Random" [{:keys [rand-range] :as params}]
  (let [start-at (rand-int rand-range)]
    (with-fetch-cards
      [#(.where % "random" ">=" start-at)
       #(.orderBy % "random")]
      params)))
