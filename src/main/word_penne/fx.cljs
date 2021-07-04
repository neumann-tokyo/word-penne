(ns word-penne.fx
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [word-penne.firebase.firestore :refer [firestore timestamp] :as fs]
            [word-penne.firebase.auth :as firebase-auth]
            [word-penne.routes :as routes]
            [word-penne.i18n :as i18n]))

(def ^:private rand-range 99999999)
(def ^:private quiz-item-count 4)

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
 (fn [{:keys [user-uid search-target search-word search-tag search-archive cards-order on-success]}] ; TODO I want to pass a sort order
   (when user-uid
     (go
       (let [[order-column order-direction] (str/split cards-order #"/")
             snapshot (<p! (as-> (firestore) f
                             (.collection f (str "users/" user-uid "/cards"))
                             (.where f "archive" "==" (boolean search-archive))
                             (if search-tag
                               (.where f "tags" "array-contains-any" #js[search-tag])
                               f)
                             (if (and search-target search-word)
                               (-> f
                                   (.orderBy search-target)
                                   (.startAt search-word)
                                   (.endAt (str search-word "\uf8ff")))
                               (.orderBy f order-column order-direction))
                             (.get f)))
             result (r/atom [])]
         (.forEach snapshot
                   (fn [doc]
                     (swap! result conj
                            (conj {:uid (.-id doc)}
                                  (js->clj (.data doc) :keywordize-keys true)))))
         (on-success @result))))))

(re-frame/reg-fx
 ::firebase-load-tags
 (fn [{:keys [user-uid on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection "users")
         (.doc user-uid)
         (.get)
         (.then
          (fn [doc]
            (when (.-exists doc)
              (when-let [tags (:tags (js->clj (.data doc) :keywordize-keys true))]
                (on-success tags)))))))))

(re-frame/reg-fx
 ::firebase-create-card
 (fn [{:keys [user-uid tags values on-success]}]
   (let [v (assoc (select-keys values ["front" "back" "comment"])
                  "tags" (mapv #(str/trim (% "name")) (remove #(= % {"name" "" "beforeName" ""}) (values "tags"))))
         initial-data {:archive false
                       :lock false
                       :quizCount 0
                       :wrongCount 0
                       :wrongRate 0.0
                       :random (rand-int rand-range)
                       :createdAt (timestamp)
                       :updatedAt (timestamp)}]
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.add (clj->js (merge initial-data v)))
         (.then (fn []
                  (-> (firestore)
                      (.collection "users")
                      (.doc user-uid)
                      (.set (clj->js {:tags (distinct (concat tags (v "tags")))}) #js {:merge true})
                      (.then on-success))))))))

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
                    (on-success (conj {:uid card-uid} (js->clj (.data doc) :keywordize-keys true))))))))))

(re-frame/reg-fx
 ::firebase-update-card-by-uid
 (fn [{:keys [user-uid card-uid tags values on-success]}]
   (let [v (assoc (select-keys values ["front" "back" "comment"])
                  "tags" (mapv #(str/trim (% "name")) (remove #(= % {"name" "" "beforeName" ""}) (values "tags"))))]
     (when user-uid
       (go
         (let [batch (.batch (firestore))
               cards-ref (-> (firestore)
                             (.collection (str "users/" user-uid "/cards"))
                             (.doc card-uid))
               users-ref (-> (firestore)
                             (.collection "users")
                             (.doc user-uid))]
           (.update batch cards-ref (clj->js (assoc v :updatedAt (timestamp))))
           (.set batch users-ref (clj->js {:tags (distinct (concat tags (v "tags")))}))
           (<p! (.commit batch))
           (on-success)))))))

(re-frame/reg-fx
 ::firebase-delete-card-by-uid
 (fn [{:keys [user-uid card-uid on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.doc card-uid)
         (.delete)
         (.then on-success)))))

(re-frame/reg-fx
 ::firebase-archive-card-by-uid
 (fn [{:keys [user-uid card-uid archive on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.doc card-uid)
         (.update #js {:archive archive :updatedAt (timestamp)})
         (.then on-success)))))

(re-frame/reg-fx
 ::firebase-load-user-setting
 (fn [{:keys [user-uid on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection "users")
         (.doc user-uid)
         (.get)
         (.then
          (fn [doc]
            (when (.-exists doc)
              (on-success (-> (js->clj (.data doc) :keywordize-keys true)
                              (update :locale #(or % "en")))))))))))

(re-frame/reg-fx
 ::firebase-update-user-setting
 (fn [{:keys [user-uid values on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection "users")
         (.doc user-uid)
         (.set #js {:locale (values "locale")} #js {:merge true})
         (.then on-success)))))

(re-frame/reg-fx
 ::firebase-lock-card-by-uid
 (fn [{:keys [user-uid card-uid lock on-success]}]
   (when user-uid
     (-> (firestore)
         (.collection (str "users/" user-uid "/cards"))
         (.doc card-uid)
         (.update #js {:lock lock :updatedAt (timestamp)})
         (.then on-success)))))

(re-frame/reg-fx
 ::firebase-update-tags
 (fn [{:keys [user-uid tags values on-success on-failure]}]
   (let [input-tags (remove #(= % {"name" "" "beforeName" ""}) (values "tags"))
         v (assoc values
                  "tags" (mapv #(str/trim (% "name")) input-tags))
         deleted-tags (set/difference (set tags)
                                      (set (map #(% "beforeName") input-tags)))
         edited-tags (filter (fn [t] (and (t "beforeName") (not= (t "name") (t "beforeName")))) input-tags)
         duplicated-tags (->> input-tags
                              (filter #(str/blank? (% "beforeName")))
                              (map #(get % "name"))
                              (filter (fn [t] ((set tags) t))))
         learge-tags (->> input-tags
                          (map #(get % "name"))
                          (filter #(>= (count %) 10)))
         error-messages (r/atom [])]
     (when user-uid
       (go
         (when (seq duplicated-tags)
           (swap! error-messages conj (str (i18n/tr "Duplicate tags: ") (str/join ", " duplicated-tags))))

         (when (seq learge-tags)
           (swap! error-messages conj (str (i18n/tr "Max length is 10: ") (str/join "," learge-tags))))

         ;; 削除対象のタグを消す。使用中の場合はエラーにする
         (when (seq deleted-tags)
           (let [snapshot (<p! (-> (firestore)
                                   (.collection (str "users/" user-uid "/cards"))
                                   (.where "tags" "array-contains-any" (clj->js deleted-tags))
                                   (.get)))]
             (when-not (.-empty snapshot)
               (swap! error-messages conj (str (i18n/tr "The tags are used: ") (str/join ", " deleted-tags))))))

         (if (seq @error-messages)
           (on-failure (str/join ". " @error-messages))
           (let [batch (.batch (firestore))]
             ;; 編集したタグを使っているカードを更新する 
             (when (seq edited-tags)
               (let [{:keys [before-names
                             after-names]} (reduce (fn [r t]
                                                     (-> r
                                                         (update :before-names conj (t "beforeName"))
                                                         (update :after-names conj (t "name"))))
                                                   {:before-names [] :after-names []}
                                                   edited-tags)
                     snapshot (<p! (-> (firestore)
                                       (.collection (str "users/" user-uid "/cards"))
                                       (.where "tags" "array-contains-any" (clj->js before-names))
                                       (.get)))]
                 (.forEach
                  snapshot
                  (fn [doc]
                    ;; TODO 500件以上ある場合batchが失敗する? 
                    ;; https://firebase.google.com/docs/firestore/manage-data/transactions
                    (doseq [n before-names]
                      (.update batch (.-ref doc) (clj->js {:tags (fs/array-remove n)})))
                    (doseq [n after-names]
                      (.update batch (.-ref doc) (clj->js {:tags (fs/array-union n)})))))))

             ;; tagsの更新
             (let [update-tags-ref (-> (firestore)
                                       (.collection "users")
                                       (.doc user-uid))]
               (.set batch update-tags-ref (clj->js v) #js {:merge true}))
             (<p! (.commit batch))
             (on-success))))))))


(defn- get-cards [snapshot]
  (let [cards (r/atom [])]
    (.forEach
     snapshot
     (fn [doc]
       (let [card (js->clj (.data doc) :keywordize-keys true)]
         (swap! cards
                conj
                {:uid (.-id doc) :front (:front card) :back (:back card) :comment (:comment card)}
                {:uid (.-id doc) :front (:back card) :back (:front card) :comment (:comment card)}))))
    @cards))

(re-frame/reg-fx
 ::firebase-setup-quiz
 (fn [{:keys [user-uid on-success]}]
   (when user-uid
     (go
       (let [half-quiz-count (/ quiz-item-count 2)
             high-wrong-snap (<p! (-> (firestore)
                                      (.collection (str "users/" user-uid "/cards"))
                                      (.orderBy "wrongRate" "desc")
                                      (.where "archive" "==" false)
                                      (.limit half-quiz-count)
                                      (.get)))
             start-at (rand-int rand-range)
             quiz-snap (<p! (-> (firestore)
                                (.collection (str "users/" user-uid "/cards"))
                                (.orderBy "random")
                                (.orderBy "wrongRate")
                                (.where "random" ">=" start-at)
                                (.where "archive" "==" false)
                                (.limit half-quiz-count)
                                (.get)))
             cards (distinct (into (get-cards high-wrong-snap)
                                   (get-cards quiz-snap)))]
         (on-success (shuffle cards)))))))

(defn- check-answer [answer correct-text]
  (cond
    (str/blank? answer) false

    (= (str/lower-case (str/trim answer))
       (str/lower-case correct-text)) true

    :else false))

(re-frame/reg-fx
 ::firebase-answer-quiz
 (fn [{:keys [user-uid values on-success]}]
   (when user-uid
     (let [judgement (if (check-answer (values "answer") (values "correct-text")) "Correct" "Wrong")
           card-ref (-> (firestore)
                        (.collection (str "users/" user-uid "/cards"))
                        (.doc (values "uid")))]
       (-> (firestore)
           (.runTransaction
            (fn [transaction]
              (-> transaction
                  (.get card-ref)
                  (.then (fn [doc]
                           (when (.-exists doc)
                             (let [card (js->clj (.data doc) :keywordize-keys true)
                                   new-quiz-count (inc (:quizCount card))
                                   wrong-count (:wrongCount card)
                                   new-wrong-count (if (= judgement "Correct")
                                                     wrong-count
                                                     (inc wrong-count))
                                   new-wrong-rate (double (/ new-wrong-count new-quiz-count))]
                               (-> transaction
                                   (.update card-ref #js {:quizCount new-quiz-count
                                                          :wrongCount new-wrong-count
                                                          :wrongRate new-wrong-rate})))))))))
           (.then (fn [] (on-success judgement))))))))
