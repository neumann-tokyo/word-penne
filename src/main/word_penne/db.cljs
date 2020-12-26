(ns word-penne.db
  (:require [cljs.spec.alpha :as s]))

(s/def ::uid string?)
(s/def ::display-name string?)
(s/def ::photo-url string?)
(s/def ::user (s/nilable (s/keys :req-un [::uid ::display-name ::photo-url])))
(s/def ::front-text string?)
(s/def ::back-text string?)
(s/def ::comment string?)
;; (s/def ::tag string?)
;; (s/def ::tags (s/map-of ::tag))
(s/def ::card (s/keys :req-un [::uid ::front-text ::back-text]
                      :opt-un [::comment])) ; TODO tag をいれる
(s/def ::cards (s/coll-of ::card))
(s/def ::selected-card (s/nilable ::card))
(s/def ::db (s/keys :req-un [::user ::cards ::selected-card]))

(def default-db
  {:user nil
   :cards []
   :selected-card nil})
