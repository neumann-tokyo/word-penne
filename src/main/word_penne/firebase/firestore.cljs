(ns word-penne.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]))

(defn firestore []
  (let [db (.firestore firebase)]
    (when (= js/location.hostname "localhost")
      (.useEmulator db "127.0.0.1" "8081"))
    db))
