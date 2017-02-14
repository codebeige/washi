(ns washi.parser-test
  (:require [clojure.test :refer [deftest testing is are]]
            [washi.parser :as parser]
            [clojure.spec :as s]))

(deftest text
  (testing "conform"
    (are [input output] (= output (s/conform ::parser/text input))
         "I need writing." "I need writing."
         28                "28"
         2/3               "2/3"
         6.7               "6.7"
         \*                "*"))
  (testing "invalid"
    (are [input] (not (s/valid? ::parser/text input))
         :indigo 'nagiko '() [] nil)))

(deftest tag
  (testing "conform"
    (are [input output] (= output (s/conform ::parser/tag input))
         :p               {:name "p"}
         :tr#nagiko       {:name "tr" :id "nagiko"}
         :#pillow-book    {:name "div" :id "pillow-book"}
         :li.item         {:name "li" :class "item"}
         :.indigo         {:name "div" :class "indigo"}
         :li.nav.selected {:name "li" :class "nav selected"}))
  (testing "invalid"
    (are [input] (not (s/valid? ::parser/tag input))
         "#pillow-book" 'div nil)))

(deftest attributes
  (testing "conform"
    (are [input output] (= output (s/conform ::parser/attributes input))
         {:name "Nagiko"} {:name "Nagiko"}
         {:colspan 3}     {:colspan "3"}
         {}               {}
         nil              nil))
  (testing "invalid"
    (are [input] (not (s/valid? ::parser/attributes input))
         '(:value "indigo") [] "Anything coloured Indigo is splendid;")))

(deftest element
  (testing "conform"
    (are [input output] (= output (s/conform ::parser/element input))
         [:div]
         {:tag "div"
          :attributes {}
          :body []}

         [:input {:value "Nagiko"}]
         {:tag "input"
          :attributes {:value "Nagiko"}
          :body []}

         [:li.quote {:class "nagiko"}]
         {:tag "li"
          :attributes {:class "quote nagiko"}
          :body []}

         [:p#quote {:id 23}]
         {:tag "p"
          :attributes {:id "quote-23"}
          :body []}

         [:div "I like the smell of paper - all kinds."]
         {:tag "div"
          :attributes {}
          :body [[:text "I like the smell of paper - all kinds."]]}

         [:p nil "It reminds me of the scent of skin."]
         {:tag "p"
          :attributes {}
          :body [[:text "It reminds me of the scent of skin."]]}

         [:p {} "Just take out your pen and write on my arm."]
         {:tag "p"
          :attributes {}
          :body [[:text "Just take out your pen and write on my arm."]]}))
  (testing "invalid"
    (are [input] (not (s/valid? ::parser/element input))
         [] '(:div) ['div] ["div"] [[]])))

(deftest template
  (testing "conform"
    (are [input output] (= output (s/conform ::parser/template input))
         "How can I get pleasure writing on you?"
         [:text "How can I get pleasure writing on you?"]

         [:p "You have to write on me."]
         [:element
          {:tag "p"
           :attributes {}
           :body [[:text "You have to write on me."]]}]

         (repeat 3 "Go on.")
         [:collection [[:text "Go on."][:text "Go on."][:text "Go on."]]]

         ["Use my body like the pages of a book." [:br] "Of your book."]
         [:collection
          [[:text "Use my body like the pages of a book."]
           [:element {:tag "br" :attributes {} :body []}]
           [:text "Of your book."]]]

         [[:p "This is a book written a long time ago."]]
         [:collection
          [[:element
            {:tag "p"
             :attributes {}
             :body [[:text "This is a book written a long time ago."]]}]]]))

  (testing "invalid"
    (are [input] (not (s/valid? ::parser/template input))
         'div :p '(:li))))
