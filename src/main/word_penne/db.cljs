(ns word-penne.db)

;; malli type: https://github.com/metosin/malli
(def t-user
  [:map
   [:uid string?]
   [:email string?]
   [:photo-url string?]])
(def t-card
  [:map
   [:uid string?]
   [:front string?]
   [:back string?]
   [:comment {:optional true} string?]])
(def t-db
  [:map
   [:user
    [:maybe t-user]]
   [:cards
    [:sequential t-card]]
   [:selected-card
    [:maybe t-card]]
   [:show-delete-card-modal boolean?]
   [:search-word [:maybe string?]]])

(def default-db
  {:user nil
   :cards []
   :selected-card nil
   :show-delete-card-modal false
   :search-word nil})
