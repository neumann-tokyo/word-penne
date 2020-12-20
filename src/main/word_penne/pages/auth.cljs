(ns word-penne.pages.auth
  (:require [reagent.core :as reagent]
            [word-penne.views :as v]
            [word-penne.firebase.auth :as f-auth]))

(defmethod v/view ::signin [_]
  (reagent/create-class
   {:component-did-mount
    (fn [_]
      (f-auth/initialize-firebaseui "#firebaseui-auth-container"))
    :reagent-render
    (fn []
      [:div "signin"
       [:div#firebaseui-auth-container]])}))
