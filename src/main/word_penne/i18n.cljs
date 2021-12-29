(ns word-penne.i18n
  (:require [re-frame.core :as re-frame]
            [word-penne.subs :as subs]))

(defonce ^:private ja-map
  {;; Navigation
   "Cards" "カード"
   "Edit tags" "タグを編集"
   "Archive" "アーカイブ"

   ;; AvatorMenu
   "Settings" "設定"
   "Logout" "ログアウト"

   ;; Setting
   "Locale" "言語設定"
   "Front speak language" "表面発話言語"
   "Back speak language" "裏面発話言語"

   ;; Header
   "Search..." "検索..."

   ;; Home 
   "Tag: " "タグ: "
   "Quiz" "クイズ"
   "Reverse" "裏返す"
   "Update" "更新順"
   "Random" "ランダム"
   "Wrong rate" "誤答率"
   "Let's take the quiz and review the registered words!!" "クイズに挑戦して登録した単語を復習しましょう!!"

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
   "Edit" "編集"
   "Make it correct" "正解にする"
   "Do you quit? The data in the middle will be deleted." "クイズを終了しますか？途中のデータは削除されます。"})

(defn- gettext [word-map text]
  (let [word (get word-map text)]
    (if word
      word
      (throw (js/Error. (str "Can't transrate: " text))))))

(defn tr [text]
  (let [locale @(re-frame/subscribe [::subs/locale])]
    (if (= locale "ja")
      (gettext ja-map text)
      text)))
