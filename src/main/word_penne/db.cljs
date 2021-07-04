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
   [:lock boolean?]
   [:quizCount int?]
   [:wrongCount int?]
   [:wrongRate double?]])
(def t-search-target
  [:enum "front" "back" "comment"])
(def t-search-word
  [:string {:min 0 :max 140}])
(def t-cards-order
  [:enum "wrongRate/desc" "updatedAt/desc" "random/asc"])
(def t-judgement
  [:enum "Correct" "Wrong"])
(def t-quiz-card
  [:map
   [:uid string?]
   [:front string?]
   [:back string?]
   [:judgement {:optional true} t-judgement]])
(def t-db
  [:map
   [:user
    [:maybe t-user]]
   [:locale t-locale]
   [:reverse-cards boolean?]
   [:cards
    [:sequential t-card]]
   [:clicked-card-uid [:maybe string?]]
   [:selected-card
    [:maybe t-card]]
   [:show-confirmation-modal boolean?]
   [:tags t-tags]
   [:tags-error [:maybe string?]]
   [:search-target t-search-target]
   [:search-word [:maybe t-search-word]]
   [:search-tag [:maybe string?]]
   [:search-archive boolean?]
   [:cards-order t-cards-order]
   [:quiz-cards
    [:sequential t-quiz-card]]
   [:quiz-pointer int?]])

(def default-db
  {:user nil
   :locale "en"
   :reverse-cards false
   :cards []
   :clicked-card-uid nil
   :selected-card nil
   :show-confirmation-modal false
   :tags []
   :tags-error nil
   :search-target "front"
   :search-word nil
   :search-tag nil
   :search-archive false
   :cards-order "wrongRate/desc"
   :quiz-cards []
   :quiz-pointer 0})
