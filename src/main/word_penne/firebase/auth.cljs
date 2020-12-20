(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["firebaseui" :as firebaseui]
            ["@firebase/auth"]))

(defn auth []
  (.auth firebase))

(defn ui []
  (.AuthUI (.-auth firebaseui) (auth)))
