(ns word-penne.core
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [stylefy.core :as stylefy]
            [word-penne.config :as config]
            [word-penne.events :as events]
            [word-penne.routes :refer [routes]]
            [word-penne.views :as views]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (re-frame/dispatch [::events/set-current-route
                                       (bidi/match-route routes path)]))
    :path-exists? (fn [path]
                    (boolean (bidi/match-route routes path)))
    :reload-same-path? true})
  (accountant/dispatch-current!)
  (dev-setup)
  (stylefy/init)
  (mount-root))
