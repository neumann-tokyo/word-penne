(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]
            ["firebaseui" :as firebaseui]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]))

(defn- auth []
  (.auth firebase))

(defn- ui []
  (try (new (.AuthUI (.-auth firebaseui) (auth)))
       (catch js/Error e
         (print e)))
  (.. firebaseui -auth -AuthUI getInstance))

(defn initialize-firebaseui [target-id]
  (-> (ui)
      (.start target-id
              #js {:signInSuccessUrl (path-for routes :word-penne.pages.home/home)
                   :signInOptions #js [(.. firebase -auth -GoogleAuthProvider -PROVIDER_ID)]})))