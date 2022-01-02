(ns word-penne.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::debug-db
 (fn [db _] db))

(re-frame/reg-sub
 ::current-route
 (fn [db _]
   (get db :route {:handler :word-penne.pages.home/home})))

(re-frame/reg-sub
 ::current-user
 (fn [db _]
   (:user db)))

(re-frame/reg-sub
 ::reverse-cards
 (fn [db _]
   (:reverse-cards db)))

(re-frame/reg-sub
 ::cards
 (fn [db _]
   (:cards db)))

(re-frame/reg-sub
 ::clicked-card-uid
 (fn [db _]
   (:clicked-card-uid db)))

(re-frame/reg-sub
 ::locked-cards
 (fn [db _]
   (filter (fn [c] (= (:lock c) true)) (:cards db))))

(re-frame/reg-sub
 ::unlocked-cards
 (fn [db _]
   (filter (fn [c] (= (:lock c) false)) (:cards db))))

(re-frame/reg-sub
 ::selected-card
 (fn [db _]
   (:selected-card db)))

(re-frame/reg-sub
 ::show-confirmation-modal
 (fn [db _]
   (:show-confirmation-modal db)))

(re-frame/reg-sub
 ::search-target
 (fn [db _]
   (:search-target db)))

(re-frame/reg-sub
 ::search-word
 (fn [db _]
   (:search-word db)))

(re-frame/reg-sub
 ::search-tag
 (fn [db _]
   (:search-tag db)))

(re-frame/reg-sub
 ::tags
 (fn [db _]
   (:tags db)))

(re-frame/reg-sub
 ::tags-error
 (fn [db _]
   (:tags-error db)))

(re-frame/reg-sub
 ::search-archive
 (fn [db _]
   (:search-archive db)))

(re-frame/reg-sub
 ::locale
 (fn [db _]
   (:locale db)))

(re-frame/reg-sub
 ::quiz-cards0
 (fn [db _]
   (:quiz-cards0 db)))

(re-frame/reg-sub
 ::quiz-cards
 (fn [db _]
   (:quiz-cards db)))

(re-frame/reg-sub
 ::quiz-card
 (fn [db _]
   (nth (:quiz-cards db) (:quiz-pointer db))))

(re-frame/reg-sub
 ::quiz-pointer
 (fn [db _]
   (:quiz-pointer db)))

(re-frame/reg-sub
 ::cards-order
 (fn [db _]
   (:cards-order db)))

(re-frame/reg-sub
 ::autocomplete-cards
 (fn [db _]
   (:autocomplete-cards db)))

(re-frame/reg-sub
 ::relational-cards
 (fn [db _]
   (:relational-cards db)))

(re-frame/reg-sub
 ::front-speak-language
 (fn [db _]
   (:front-speak-language db)))

(re-frame/reg-sub
 ::back-speak-language
 (fn [db _]
   (:back-speak-language db)))

(re-frame/reg-sub
 ::quiz-settings
 (fn [db _]
   (:quiz-settings db)))
