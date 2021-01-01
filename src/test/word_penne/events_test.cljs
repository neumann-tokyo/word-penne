(ns word-penne.events-test
  (:require [cljs.test :as t]
            ["node-fetch" :default fetch]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))

;; clean up the database
(t/use-fixtures :each
  {:before
   (fn []
     (let [res (fetch "http://localhost:8081/emulator/v1/projects/word-penne/databases/(default)/documents" #js {:method "DELETE"})]
       (t/async done
                (go
                  (<p! res)
                  (done)))))})

(t/deftest a-failure-test
  (t/is (= 1 2)))

(t/deftest a-success-test
  (t/is (= 1 1)))
