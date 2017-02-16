(set-env!
  :source-paths #{"src" "test"}
  :dependencies '[[org.clojure/clojure "1.9.0-alpha14" :scope "provided"]
                  [org.clojure/clojurescript "1.9.473" :scope "provided"]
                  [adzerk/boot-test "1.2.0" :scope "test"]
                  [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]])

(require '[adzerk.boot-test :refer [test]]
         '[crisptrutski.boot-cljs-test :refer [test-cljs]])

(task-options!
 test {:include #"-test$"})
