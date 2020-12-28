(ns word-penne.events-test
  (:require [cljs.test :as t]))

(t/deftest a-failure-test
  (t/is (= 1 2)))

(t/deftest a-success-test
  (t/is (= 1 1)))
