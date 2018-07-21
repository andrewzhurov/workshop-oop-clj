(ns workshop.core
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]))

(defn l [desc expr] (println desc expr) expr)
(defn to-channel [z]
  (let [next-z (zip/next z)
        next-node (zip/node next-z)]
    (if (= (:tag next-node) :channel)
      next-z
      (recur next-z))))


(defn reverse-items [feed]
  (-> feed
      to-channel
      (zip/edit update :content reverse)))

(defn process [feed]
  (-> feed
      zip/xml-zip

      reverse-items

      zip/root
      ))

(defn parse [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn unparse [feed]
  (with-out-str (xml/emit feed)))

(defn -main [path-in path-out]
  (println "Paths:" path-in path-out)
  (->> (slurp path-in)
       parse
       process
       unparse
       (spit path-out)))
