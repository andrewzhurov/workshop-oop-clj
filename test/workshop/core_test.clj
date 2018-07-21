(ns workshop.core-test
  (:require [workshop.core :as sut]
            [clojure.test :refer [deftest testing] :as t]
            [matcho.core :refer [match]]))

(def feed
  {:tag :rss,
   :attrs {:version "2.0"},
   :content
   [{:tag :channel,
     :attrs nil,
     :content
     [{:tag :title, :attrs nil, :content ["W3Schools Home Page"]}
      {:tag :link, :attrs nil, :content ["https://www.w3schools.com"]}
      {:tag :description,
       :attrs nil,
       :content ["Free web building tutorials"]}
      {:tag :item,
       :attrs nil,
       :content
       [{:tag :title, :attrs nil, :content ["RSS Tutorial"]}
        {:tag :link,
         :attrs nil,
         :content ["https://www.w3schools.com/xml/xml_rss.asp"]}
        {:tag :description,
         :attrs nil,
         :content ["New RSS tutorial on W3Schools"]}]}
      {:tag :item,
       :attrs nil,
       :content
       [{:tag :title, :attrs nil, :content ["XML Tutorial"]}
        {:tag :link,
         :attrs nil,
         :content ["https://www.w3schools.com/xml"]}
        {:tag :description,
         :attrs nil,
         :content ["New XML tutorial on W3Schools"]}]}]}]})

(deftest processing
  (testing "Channel processing"
    (t/is (= 1 1))
    (match (sut/process [] feed)
           feed)

    (match (sut/process ["--reverse"] feed)
           {:tag :rss
            :content [{:tag :channel
                       :content [{:tag :title}
                                 {:tag :link}
                                 {:tag :description}
                                 {:tag :item
                                  :content
                                  [{:tag :title, :attrs nil, :content ["XML Tutorial"]}]}
                                 {:tag :item
                                  :content
                                  [{:tag :title, :attrs nil, :content ["RSS Tutorial"]}]}]}]})))

(defn l [desc expr] (println desc expr) expr)
(defn -main []
  (println "Running tests")
  (t/run-tests 'workshop.core-test))
