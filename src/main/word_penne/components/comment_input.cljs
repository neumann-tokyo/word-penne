(ns word-penne.components.comment-input
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            ["textarea-caret" :as getCaretCoordinates]
            [word-penne.style.form :as sf]
            [word-penne.style.vars :refer [z-indexs]]
            [word-penne.subs :as subs]
            [word-penne.events :as events]))

(def s-card-selector
  (merge sf/s-text
         {:position "absolute"
          :display "none"
          :margin 0
          :padding 0
          :width "10rem"
          :z-index (:hashtag-selection z-indexs)}))

(defonce comment-box (r/atom {}))
(defonce show-card-selector (r/atom false))
(defonce !comment (atom nil))
(defonce !cards-selector (atom nil))

(defn- cursor-position [{:keys [x y caret]}]
  {:cursor-x (+ x (:left caret))
   :cursor-y (+ y (:top caret))})

(defn CommentInput [{:keys [values handle-change set-values handle-blur]}]
  [:<>
   [:textarea (use-style
               sf/s-text
               {:id "comment"
                :name "comment"
                :data-testid "word-card-form__comment"
                :value (values "comment")
                :ref (fn [e] (reset! !comment e))
                :on-change (fn [e]
                             (let [comment-dom (.-target e)
                                   current-text (.-value comment-dom)
                                   cursor-position (.-selectionEnd comment-dom)]
                               (handle-change e)
                               (when (= (get current-text (dec cursor-position)) "#")
                                 (set-values {"cards-selector" ""})
                                 (reset! comment-box {:x (.-offsetLeft comment-dom)
                                                      :y (.-offsetTop comment-dom)
                                                      :caret (js->clj
                                                              (getCaretCoordinates comment-dom cursor-position)
                                                              :keywordize-keys true)
                                                      :cursor-position cursor-position
                                                      :text current-text})
                                 (when-let [cards-selector @!cards-selector]
                                    ;; NOTE show-card-selector だけでは表示する前に focus を当てようとして失敗するので
                                    ;; 強制的に一度 display を書き換えている
                                   (set! (.. cards-selector -style -display) "inline-block")
                                   (reset! show-card-selector true)
                                   (.focus cards-selector)))))
                :on-blur handle-blur
                :rows 3})]

   (let [{:keys [cursor-x cursor-y]} (cursor-position @comment-box)
         set-comment (fn [e]
                       (let [current-text (-> e .-target .-value)
                             [first-half latter-half] (split-at (:cursor-position @comment-box) (:text @comment-box))
                             new-comment (str (str/join first-half) current-text (str/join latter-half))]
                         (set-values {"comment" new-comment})))
         close (fn [e]
                 (set-values {"cards-selector" ""})
                 (reset! show-card-selector false)
                 (set! (.. e -target -style -display) "none")
                 (when-let [comment @!comment]
                   (.focus comment)))]
     [:input (use-style (merge s-card-selector
                               {:top (str cursor-y "px")
                                :left (str cursor-x "px")
                                :display (if @show-card-selector "inline-block" "none")})
                        {:type "text"
                         :list "cards-list"
                         :id "cards-selector"
                         :name "cards-selector"
                         :key "cards-selector"
                         :required false
                         :value (values "cards-selector")
                         :ref (fn [e] (reset! !cards-selector e))
                         :on-key-down (fn [e]
                                        (case (.-keyCode e)
                                          27 ;; Escape
                                          (do
                                            (.preventDefault e)
                                            (close e)
                                            false)

                                          13 ;; return
                                          (do
                                            (.preventDefault e)
                                            (set-comment e)
                                            (close e)
                                            false)

                                          e ;; Default
                                          ))
                         :on-change (fn [e]
                                      (let [current-text (-> e .-target .-value)]
                                        (if (str/ends-with? current-text " ")
                                          (do
                                            (set-comment e)
                                            (close  e))
                                          (do
                                            (re-frame/dispatch [::events/fetch-autocomplete-cards {:search-word current-text}])
                                            (handle-change e)))))
                         :on-blur handle-blur})])

   [:datalist {:id "cards-list"}
    (doall (map-indexed
            (fn [index value]
              [:option {:value value :key index}])
            @(re-frame/subscribe [::subs/autocomplete-cards])))]])
