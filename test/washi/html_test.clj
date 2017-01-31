(ns washi.html-test
  (:require [clojure.test :refer [deftest testing is]]
            [washi.html :as html]))

(deftest render
  (is (= "<div></div>" (html/render [:div]))))
