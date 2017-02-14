(ns washi.parser
  (:require [clojure.spec :as s]
            [clojure.string :as string]))

(def text? (some-fn string? number? char?))

(defn text [n]
  (if (text? n) (str n) ::s/invalid))

(s/def ::text (s/conformer text))

(defn tag-id [s]
  (if (string/starts-with? s "#") (subs s 1) ::s/invalid))

(defn tag-class [s]
  (if (string/starts-with? s ".") (subs s 1) ::s/invalid))

(defn append [m k v & {:keys [delimiter] :or {delimiter " "}}]
  (update m k #(if (string/blank? %) v (str % delimiter v))))

(defn merge-attrs-kv [m k v]
  (case k
    :class (append m k v)
    :id (append m k v :delimiter "-")
    (assoc m k v)))

(defn merge-attrs [& attrs]
  (reduce #(reduce-kv merge-attrs-kv %1 %2) attrs))

(defn conform-tag [n]
  (->> (name n)
       (re-seq #"[#.]?[^#.]+")
       (s/conform (s/coll-of (s/or :class (s/conformer tag-class)
                                   :id (s/conformer tag-id)
                                   :name string?)))
       (map #(apply hash-map %))
       (apply merge-attrs {:name "div"})))

(defn tag [n]
  (if (keyword? n) (conform-tag n) ::s/invalid))

(s/def ::tag (s/conformer tag))

(s/def ::attributes (s/nilable (s/map-of keyword? ::text)))

(defn element [n]
  (let [{:keys [tag attributes body] :or {body []} :as el}
        (s/conform (s/and vector?
                          (s/cat :tag ::tag
                                 :attributes (s/? ::attributes)
                                 :body (s/* ::template)))
                   n)]
    (if-not (= el ::s/invalid)
      {:tag (:name tag)
       :attributes (-> (select-keys tag [:id :class])
                       (merge-attrs attributes))
       :body body}
      ::s/invalid)))

(s/def ::element (s/conformer element))

(s/def ::template (s/or :text ::text
                        :element ::element
                        :collection (s/coll-of ::template :kind sequential?)))

(defn parse [t]
  (s/conform ::element t))
