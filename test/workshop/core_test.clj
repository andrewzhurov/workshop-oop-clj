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
     [{:tag :id, :attrs nil, :content ["1"]}
      {:tag :title, :attrs nil, :content ["Feed item 1"]}
      {:tag :link,
       :attrs nil,
       :content ["https://www.w3schools.com/xml/xml_rss.asp"]}
      {:tag :description,
       :attrs nil,
       :content ["New RSS tutorial on W3Schools"]}
      {:tag :pubDate, :attrs nil, :content ["2009-06-09T13:45:30"]}]}
    {:tag :item,
     :attrs nil,
     :content
     [{:tag :id, :attrs nil, :content ["2"]}
      {:tag :title, :attrs nil, :content ["Feed item 2"]}
      {:tag :link,
       :attrs nil,
       :content ["https://www.w3schools.com/xml/xml_rss.asp"]}
      {:tag :description,
       :attrs nil,
       :content ["New RSS tutorial on W3Schools"]}
      {:tag :pubDate, :attrs nil, :content ["2009-06-10T13:45:30"]}]}
    {:tag :item,
     :attrs nil,
     :content
     [{:tag :id, :attrs nil, :content ["3"]}
      {:tag :title, :attrs nil, :content ["Feed item 3"]}
      {:tag :link, :attrs nil, :content ["https://www.feed3.com"]}
      {:tag :description, :attrs nil, :content ["Feed description 3"]}
      {:tag :pubDate,
       :attrs nil,
       :content ["2009-06-07T13:45:30"]}]}]}]})

(defn feed-item [id]
  {:tag :item,
   :content
   [{:tag :id, :attrs nil, :content [id]}]})

(deftest processing
  (testing "Channel processing"
    (t/is (= 1 1))
    (match (sut/process {} feed)
           feed)

    (match (sut/process {:do-reverse true} feed)
           {:tag :rss
            :content [{:tag :channel
                       :content [{:tag :title}
                                 {:tag :link}
                                 {:tag :description}
                                 (feed-item "3")
                                 (feed-item "2")
                                 (feed-item "1")]}]})

    (match (sut/process {:do-sort true} feed)
           {:tag :rss
            :content [{:tag :channel
                       :content [{:tag :title}
                                 {:tag :link}
                                 {:tag :description}
                                 (feed-item "2")
                                 (feed-item "1")
                                 (feed-item "3")]}]})

    (match {:a 2} {:a pos?})
    (match (sut/process {:limit 2} feed)
           {:tag :rss
            :content [{:tag :channel
                       :content #(= (count %) 5)}]})))

(defn l [desc expr] (println desc expr) expr)
(defn -main []
  (println "Running tests")
  (t/run-tests 'workshop.core-test))
