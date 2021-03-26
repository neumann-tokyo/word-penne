(ns word-penne.i18n
  (:require ["gettext.js" :as gettext]))

(def i18n-map
  #js {"Cards" "カード"})

(defn i18n []
  (let [g (gettext)]
    (.setMessages g "messages" "ja" i18n-map)
    ;(.setLocale g "ja")
    g))

;; TODO locale を user setting で設定できるようにする
; locate is "ja" or "en"
(defn set-locale [i18n locale]
  (.setLocale i18n locale))
