(ns word-penne.pages.auth
  (:require [stylefy.core :as stylefy :refer [use-style]]
            [reagent.core :as reagent]
            [word-penne.views :as v]
            [word-penne.style.share :as share]
            [word-penne.firebase.auth :as f-auth]))

(def s-signin-container
  (merge share/m-card
         {:margin "0 auto"
          :width "70%"}))

(defmethod v/view ::signin [_]
  (reagent/create-class
   {:component-did-mount
    (fn [_]
      (f-auth/initialize-firebaseui "#firebaseui-auth-container"))
    :reagent-render
    (fn []
      [:div (use-style s-signin-container)
       [:h1 "Sign In / Sign Up"]
       [:ul
        [:li
         [:a {:href "https://word-penne-lp.neumann.tokyo/terms_of_service_en"} "Term of service"]]
        [:li
         [:a {:href "https://word-penne-lp.neumann.tokyo/terms_of_service_jp"} "利用規約"]]
        [:li
         [:a {:href "https://word-penne-lp.neumann.tokyo/privacy"} "Privacy Policy"]]]

       [:div#firebaseui-auth-container]])}))
