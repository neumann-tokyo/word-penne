(ns tools.seeds
  (:require ["fs" :as fs]
            ["firebase-admin" :as admin]))

(def ^:private config
  {:databaseURL "http://localhost:9100/firestore"
   :user-uid "7jYxVKp0qCgH4RKT4qSomG03pXky"})

(def ^:private rand-range 10000000)

(defn- initialize-firebase []
  (.initializeApp admin #js {:credential (-> admin .-credential .applicationDefault)
                             :databaseURL (:databaseURL config)}))

(defn- load-json-data []
  (-> (.readFileSync fs "data/1_Basic300.json" "utf8")
      (js/JSON.parse)
      (js->clj)))

(defn timestamp []
  (.. admin -firestore -Timestamp now))

(defn main [& cli-args]
  (initialize-firebase)
  (let [firestore (.firestore admin)]
    (doseq [d (load-json-data)]
      (-> firestore
          (.collection (str "users/" (:user-uid config) "/cards"))
          (.add #js {:front (d "name")
                     :back (d "description")
                     :archive false
                     :lock false
                     :quizCount 0
                     :wrongCount 0
                     :wrongRate 0.0
                     :random (rand-int rand-range)
                     :tags #js []
                     :createdAt (timestamp)
                     :updatedAt (timestamp)})
          (.then (fn [] nil))))))
