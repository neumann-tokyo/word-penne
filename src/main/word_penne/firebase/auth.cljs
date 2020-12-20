(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]
            ["firebaseui" :as firebaseui]
            [re-frame.core :as re-frame]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]
            [word-penne.events :as events]))

(defn auth []
  (.auth firebase))

(defn check-auth []
  (-> (auth)
      (.onAuthStateChanged
       (fn [user]
         (when user
           (re-frame/dispatch [::events/set-current-user
                               {:uid (.-uid user)
                                :display-name (.-displayName user)
                                :photo-url (.-photoURL user)}]))))))

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
