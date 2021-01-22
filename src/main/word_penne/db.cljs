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
(def t-search-target
  [:enum "front" "back" "comment"])
(def t-search-word
  [:string {:min 0 :max 140}])
(def t-db
  [:map
   [:user
    [:maybe t-user]]
   [:cards
    [:sequential t-card]]
   [:selected-card
    [:maybe t-card]]
   [:show-delete-card-modal boolean?]
   [:search-target t-search-target]
   [:search-word [:maybe t-search-word]]])

(def default-db
  {:user nil
   :cards []
   :selected-card nil
   :show-delete-card-modal false
   :search-target "front"
   :search-word nil})
