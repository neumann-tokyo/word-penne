(ns word-penne.i18n
  (:require ["gettext.js" :as gettext-js]
            [reagent.core :as r]))

(def ^:private i18n-map
  #js {;; Navigation
       "Cards" "カード"
       "Edit tags" "タグを編集"
       "Archive" "アーカイブ"

       ;; AvatorMenu
       "Settings" "設定"
       "Logout" "ログアウト"

       ;; Setting
       "Locale" "言語設定"

       ;; Header
       "Search..." "検索..."

       ;; Home 
       "Tag: " "タグ: "
       "Quiz" "クイズ"

       ;; Card
       "Front" "表面"
       "Back" "裏面"
       "Comment" "コメント"
       "Tags" "タグ"

       ;;Form
       "Submit" "決定"
       "Cancel" "キャンセル"
       "missing required key" "必須項目です"
       "should be between 1 and 140 characters" "1 から 140 文字で入力してください"

       ;; Tags form
       "Duplicate tags: " "重複したタグがあります: "
       "Max length is 10: " "10文字以下にしてください: "
       "The tags are used: " "使用中のタグです: "

       ;; Delete card modal
       "Confirmation" "確認"
       "Do you want to delete, really? This action don't return" "本当に削除しますか？この操作は戻せません"
       "OK" "実行"

       ;; Quiz
       "Correct" "正解"
       "Wrong" "誤答"
       "Next" "次へ"
       "Result" "結果"
       "Answer" "正解"
       "Finish" "終了"
       "Do you quit? The data in the middle will be deleted." "クイズを終了しますか？途中のデータは削除されます。"})

(def ^:private gettext-object (r/atom nil))

(defn i18n []
  (if @gettext-object
    @gettext-object
    (let [g (gettext-js)]
      (.setMessages g "messages" "ja" i18n-map)
      (reset! gettext-object g))))

; locate is "ja" or "en"
(defn set-locale [locale]
  (.setLocale (i18n) locale))

(defn tr [text]
  (.gettext (i18n) text))
