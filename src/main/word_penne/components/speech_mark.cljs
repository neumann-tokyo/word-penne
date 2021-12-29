(ns word-penne.components.speech-mark)

(defn SpeechMark [{:keys [text lang]}]
  (let [utterance (js/SpeechSynthesisUtterance. text)
        lang (or lang "en-US")]
    (set! (.-lang utterance) lang)
    [:span {:class "material-icons-outlined"
            :on-click (fn [e]
                        (.preventDefault e)
                        (.stopPropagation e)
                        (js/speechSynthesis.speak utterance))} "play_circle"]))
