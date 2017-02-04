(ns washi.parser-test
  (:require [clojure.test :refer [deftest testing is are]]
            [washi.parser :as parser]))

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
