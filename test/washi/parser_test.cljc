(ns washi.parser-test
  (:require [clojure.test :refer [deftest testing is are]]
            [washi.parser :as parser]
            [clojure.spec :as s]))

(deftest text?
  (testing "valid"
    (are [input] (s/valid? ::parser/text input)
         "I like the smell of paper - all kinds."
         28
         2/3
         4.56))

  (testing "invalid"
    (are [input] (not (s/valid? ::parser/text input))
         :indigo
         'nagiko
         '()
         []
         nil)))

(deftest text
  (testing "conform"
    (are [input output] (= output (s/conform ::parser/text input))
         "I need writing." "I need writing."
         28                "28"
         2/3               "2/3"
         6.7               "6.7")))

(deftest parse
  (are [input output] (= output (parser/parse input))

       "I like the smell of paper - all kinds."
       [:text "I like the smell of paper - all kinds."]

       [:hr]
       [:element {:tag {:name "hr"}}]

       [:input {:value "Nagiko"}]
       [:element {:tag {:name "input"
                        :attributes {:value "Nagiko"}}}]

       [:p "You have to write on me."]
       [:element {:tag {:name "p"}
                  :body [[:text "You have to write on me."]]}]

       (repeat 2 [:li])
       [:collection [[:element {:tag {:name "li"}}]
                     [:element {:tag {:name "li"}}]]]

       [[:li] [:li]]
       [:collection [[:element {:tag {:name "li"}}]
                     [:element {:tag {:name "li"}}]]]))
