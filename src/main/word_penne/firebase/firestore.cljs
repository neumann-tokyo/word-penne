(ns word-penne.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]))

(def db (atom nil))

(defn firestore []
  (if @db
    @db
    (let [-db (.firestore firebase)]
      (when (= js/location.hostname "localhost")
        ; cypress 用の設定
        (.settings -db #js {:experimentalForceLongPolling true})
        (.useEmulator -db "localhost" 8081))
      (reset! db -db))))
