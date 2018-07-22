(ns workshop.core
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.pprint]))

(defn l [desc expr] (println desc expr) expr)
(defn to-items [z]
  (let [next-z (zip/next z)
        next-node (zip/node next-z)]
    (if (#{:channel :feed} (:tag next-node))
      next-z
      (recur next-z))))

(defn get-date [item]
  (some (fn [{:keys [tag content]}]
          (when (= tag :pubDate) (first content)))
        (:content item)))

(defn process-items [container {:keys [do-reverse do-sort limit]}]
  (let [{items true
         non false} (group-by #(= :item (:tag %)) container)]
    (cond->> items
      do-sort ((fn [i] (reverse (sort-by get-date i))))
      do-reverse reverse
      limit (take limit)
      :make-whole (concat non))))

(defmulti to-atom :tag) 
(defmethod to-atom :rss
  [el]
  (l "On RSS!!!!" 11)
  (l "El:" el)
  (l "Out:" {:tag :feed
             :content (vec (get-in el [:content 0 :content]))}))
(defmethod to-atom :default [in] (l "INNN"in))

(defmulti convert-feed (fn [_ to] to))
(defmethod convert-feed :atom
  [feed _]
  (loop [z (l "Z on loop:" (zip/xml-zip (zip/root feed)))]
    (let [new-z (zip/edit z (fn [x] (l "Node value:" x)) #_to-atom)]
      (if (zip/end? new-z)
        new-z
        (recur (zip/next new-z))))))

(defn process [args feed]
  (-> feed
      zip/xml-zip

      to-items
      (zip/edit update :content process-items args)

      (convert-feed (:convert-to args))
      ))

(defn parse [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn unparse [feed]
  (with-out-str (xml/emit feed)))

(defn -main [& [path-in path-out :as args]]
  (println "All args:" (pr-str args))
  (->> (slurp path-in)
       parse
       (process {:convert-to :atom})
       ;unparse
       ;(spit path-out)
       ))
