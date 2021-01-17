(ns word-penne.core
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [stylefy.core :as stylefy]
            [word-penne.config :as config]
            [word-penne.events :as events]
            [word-penne.subs :as subs]
            [word-penne.routes :refer [routes]]
            [word-penne.pages.main-panel :as main-panel]
            [word-penne.firebase.init :refer [initialize-firebase]]
            [word-penne.firebase.auth :as firebase-auth]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel/main-panel] root-el)))

(defn firebase-check-auth
  "Set the state if the user has signed in"
  []
  (-> (firebase-auth/auth)
      (.onAuthStateChanged
       (fn [user]
         (when user
           (re-frame/dispatch-sync [::events/set-current-user
                                    {:uid (.-uid user)
                                     :email (.-email user)
                                     :photo-url (or (.-photoURL user) "/images/account_circle-24px.svg")}])
           (re-frame/dispatch-sync [::events/navigate :word-penne.pages.home/home]))))))

(defn ^:export init []
  (initialize-firebase)
  (re-frame/dispatch-sync [::events/initialize-db])
  (firebase-check-auth)
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (re-frame/dispatch [::events/set-current-route (bidi/match-route routes path)])
                   (when-not (or @(re-frame/subscribe [::subs/current-user]) (= path (bidi/path-for routes :word-penne.pages.auth/signin)))
                     (re-frame/dispatch [::events/navigate :word-penne.pages.auth/signin])))
    :path-exists? (fn [path]
                    (boolean (bidi/match-route routes path)))
    :reload-same-path? false})
  (accountant/dispatch-current!)
  (dev-setup)
  (stylefy/init)
  (mount-root))
