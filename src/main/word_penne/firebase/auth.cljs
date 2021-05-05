(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]
            ["firebaseui" :as firebaseui]
            [reagent.core :as r]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.config :as config]))

(def auth-instance (r/atom nil))

(defn auth []
  (if @auth-instance
    @auth-instance
    (let [auth (.auth firebase)]
      (when config/debug?
        (.useEmulator auth "http://localhost:9099/")
        (.signInWithCredential auth
                               ((.. firebase -auth -GoogleAuthProvider -credential)
                                (js/JSON.stringify #js {:sub "abc123"
                                                        :email "foo@example.com"
                                                        :email_verified true}))))
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
