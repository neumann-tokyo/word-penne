(ns word-penne.firebase.init
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/analytics"]))

(goog-define API_KEY "")
(goog-define AUTH_DOMAIN "")
(goog-define PROJECT_ID "")
(goog-define STORAGE_BUCKET "")
(goog-define MESSAGING_SENDER_ID "")
(goog-define APP_ID "")
(goog-define MEASUREMENT_ID "")

(defn initialize-firebase []
  (if (zero? (count (.-apps firebase)))
    (-> firebase
        (.initializeApp
         #js {:apiKey API_KEY
              :authDomain AUTH_DOMAIN
              :projectId PROJECT_ID
              :storageBucket STORAGE_BUCKET
              :messagingSenderId MESSAGING_SENDER_ID
              :appId APP_ID
              :measurementId MEASUREMENT_ID}))
    (.-app firebase))
  (.analytics firebase))
