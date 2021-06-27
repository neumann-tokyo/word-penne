(ns word-penne.components.speech-mark)

(defn SpeechMark [text]
  (let [utterance (js/SpeechSynthesisUtterance. text)]
    [:span {:class "material-icons-outlined"
            :on-click (fn [e]
                        (.preventDefault e)
                        (.stopPropagation e)
                        (js/speechSynthesis.speak utterance))} "play_circle"]))
