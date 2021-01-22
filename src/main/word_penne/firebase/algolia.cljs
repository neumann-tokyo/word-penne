(ns word-penne.firebase.algolia
  (:require ["algoliasearch" :refer (algoliasearch)]))

(goog-define ALGOLIA_ID "")
(goog-define ALGOLIA_ADMIN_KEY "")
(goog-define ALGOLIA_SEARCH_KEY "")
(goog-define ALGOLIA_INDEX_NAME "")

(def ^:private algolia-client (atom nil))
(def ^:private index (atom nil))

(defn algolia []
  (if @algolia-client
    @algolia-client
    (let [client (algoliasearch ALGOLIA_ID, ALGOLIA_ADMIN_KEY)]
      (reset! algolia-client client))))

(defn algolia-index []
  (if @index
    @index
    (let [i (.initIndex (algolia) ALGOLIA_INDEX_NAME)]
      (reset! index i))))
