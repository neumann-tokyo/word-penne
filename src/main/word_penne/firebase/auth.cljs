(ns word-penne.firebase.auth
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]
            ["firebaseui" :as firebaseui]
            [bidi.bidi :refer [path-for]]
            [word-penne.routes :refer [routes]]))

(defn auth []
  (let [auth (.auth firebase)]
    (when (= js/location.hostname "localhost")
      (.useEmulator auth "http://127.0.0.1:9099/"))
    auth))

;; TODO FIXME 例外が発生するときに画面遷移がうまく行かない
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
