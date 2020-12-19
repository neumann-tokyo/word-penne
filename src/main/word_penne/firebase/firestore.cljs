(ns word-penne.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]))

(defn firestore []
  (.firestore firebase))

; (-> firestore
;     (.collection "cards")
;     (.get)
;     (.then (fn [snapshot]
;              (-> snapshot
;                  (.forEach (fn [doc]
;                              (js/console.log (.data doc))))))))
