(ns washi.html)

(defn render [template]
  (let [t (-> template first name)]
    (str "<" t "></" t ">")))
