(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]
            ["firebaseui" :as firebaseui]))

(defn auth []
  (.auth firebase))

(defn ui []
  (try (new (.AuthUI (.-auth firebaseui) (auth)))
       (catch js/Error e
         (print e)))
  (.. firebaseui -auth -AuthUI getInstance))

(defn initialize-firebaseui [target-id]
  (-> (ui)
      (.start target-id
              #js {:signInSuccessUrl "/" ; TODO path-for で取るようにする?
                   :signInOptions #js [(.. firebase -auth -GoogleAuthProvider -PROVIDER_ID)]})))
