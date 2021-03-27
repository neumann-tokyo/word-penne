(ns word-penne.i18n
  (:require ["gettext.js" :as gettext]))

(def ^:private gettext-object (atom nil))

(def ^:private i18n-map
  #js {"Cards" "カード"
       "Edit tags" "タグを編集"
       "Archive" "アーカイブ"
       "Settings" "設定"
       "Logout" "ログアウト"})

(defn i18n []
  (if @gettext-object
    @gettext-object
    (let [g (gettext)]
      (.setMessages g "messages" "ja" i18n-map)
      (reset! gettext-object g))))

; locate is "ja" or "en"
(defn set-locale [g locale]
  (.setLocale g locale))
