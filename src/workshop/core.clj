(ns workshop.core
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.pprint]))

(defn l [desc expr] (println desc expr) expr)
(defn to-channel [z]
  (let [next-z (zip/next z)
        next-node (zip/node next-z)]
    (if (= (:tag next-node) :channel)
      next-z
      (recur next-z))))


(defn process-channel [channel args]
  
  #_(cond->
      (contains? (set args) "--reverse") reverse-items)
  #_(-> feed
      to-channel
      (zip/edit update :content reverse)))

(defn process [args feed]
  (-> feed
      zip/xml-zip

      to-channel
      (zip/edit update :content process-channel args)

      zip/root
      ))

(defn parse [s]
  (l "Parsed:" (clojure.pprint/pprint (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))))

(defn unparse [feed]
  (with-out-str (xml/emit feed)))

(defn -main [& [path-in path-out :as args]]
  (println "All args:" (pr-str args))
  (->> (slurp path-in)
       parse
       (process args)
       unparse
       (spit path-out)))
