(ns word-penne.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]
            [reagent.core :as r]
            [word-penne.config :as config]))

(def db (r/atom nil))

(defn firestore []
  (if @db
    @db
    (let [-db (.firestore firebase)]
      (when config/debug?
        ; cypress 用の設定
        (.settings -db #js {:experimentalForceLongPolling true})
        (.useEmulator -db "localhost" 8081))
      (reset! db -db))))

(defn timestamp []
  (.. firebase -firestore -Timestamp now))

(defn array-union [value]
  ((.. firebase -firestore -FieldValue -arrayUnion) value))

(defn array-remove [value]
  ((.. firebase -firestore -FieldValue -arrayRemove) value))

(defn document-id []
  (.. firebase -firestore -FieldPath documentId))
