(ns word-penne.fx
  (:require [re-frame.core :as re-frame]
            [word-penne.routes :as routes]))

(re-frame/reg-fx
 ::navigate
 (fn [{:keys [view params]}]
   (routes/navigate view params)))
