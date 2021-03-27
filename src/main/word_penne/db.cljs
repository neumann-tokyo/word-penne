(ns word-penne.db)

;; malli type: https://github.com/metosin/malli
(def t-user
  [:map
   [:uid string?]
   [:email string?]
   [:photo-url string?]])
(def t-locale
  [:enum "en" "ja"])
(def t-tags
  [:sequential string?])
(def t-card
  [:map
   [:uid string?]
   [:front string?]
   [:back string?]
   [:comment {:optional true} [:maybe string?]]
   [:tags t-tags]
   [:createdAt {:optional true} [:maybe any?]]
   [:updatedAt {:optional true} [:maybe any?]]
   [:archive boolean?]
   [:lock boolean?]])
(def t-search-target
  [:enum "front" "back" "comment"])
(def t-search-word
  [:string {:min 0 :max 140}])
(def t-db
  [:map
   [:user
    [:maybe t-user]]
   [:locale t-locale]
   [:cards
    [:sequential t-card]]
   [:selected-card
    [:maybe t-card]]
   [:show-delete-card-modal boolean?]
   [:tags t-tags]
   [:tags-error [:maybe string?]]
   [:search-target t-search-target]
   [:search-word [:maybe t-search-word]]
   [:search-tag [:maybe string?]]
   [:search-archive boolean?]])

(def default-db
  {:user nil
   :locale "en"
   :cards []
   :selected-card nil
   :show-delete-card-modal false
   :tags []
   :tags-error nil
   :search-target "front"
   :search-word nil
   :search-tag nil
   :search-archive false})
