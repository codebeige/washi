(ns washi.parser
  (:require [clojure.spec :as s]))

(s/def ::attributes (s/map-of keyword? string?))

(defn tag-name [n]
  (if (keyword? n) (name n) :clojure.spec/invalid))

(s/def ::tag-name (s/conformer tag-name keyword))

(s/def ::element (s/cat :tag (s/cat :name ::tag-name
                                    :attributes (s/? ::attributes))
                        :body (s/* ::template)))

(s/def ::template (s/or :text string?
                        :element ::element
                        :collection (s/coll-of ::template)))

(defn parse [t]
  (s/conform ::template t))
