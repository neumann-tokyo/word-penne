(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]
            ["firebaseui" :as firebaseui]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]))

(def auth-instance (atom nil))

(defn auth []
  (if @auth-instance
    @auth-instance
    (let [auth (.auth firebase)]
      (when (= js/location.hostname "localhost")
        (.useEmulator auth "http://localhost:9099/")
        (.signInWithCredential auth
                               ((.. firebase -auth -GoogleAuthProvider -credential)
                                "{\"sub\": \"abc123\", 
                               \"email\": \"foo@example.com\", 
                               \"email_verified\": true}")))
      (reset! auth-instance auth))))

(defn- ui []
  (try (new (.. firebaseui -auth -AuthUI) (auth))
       (catch js/Object e
         (js/console.log e)))
  (.. firebaseui -auth -AuthUI getInstance))

(defn initialize-firebaseui [target-id]
  (-> (ui)
      (.start target-id
              #js {:signInSuccessUrl (path-for routes :word-penne.pages.home/home)
                   :signInOptions #js [(.. firebase -auth -GoogleAuthProvider -PROVIDER_ID)]})))
