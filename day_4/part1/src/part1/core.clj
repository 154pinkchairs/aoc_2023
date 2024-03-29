(ns part1.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(comment
  "too high :(")

(defn parse-card [line]
  (when line
    ; cut Card$number:
    (let [cleaned (str/replace line #"^Card\s+\d+: " "")
          ; trim potential excess whitespace(s) from the card numbers
          winning-numbers (str/replace (first (str/split cleaned #"\|")) #"\s+" " ")
          card-numbers (str/replace (second (str/split cleaned #"\|")) #"\s+" " ")]
      ; debug print
      (println "winning-numbers: " winning-numbers)
      (println "card-numbers: " card-numbers)
      (if (nil? card-numbers)
        (throw (ex-info "Invalid input" {:line cleaned}))
        (let [winning-set (set (map #(Integer/parseInt %) (filter seq (str/split winning-numbers #" "))))
              card-numbers (map #(Integer/parseInt %) (filter seq (str/split card-numbers #" ")))]
          [winning-set card-numbers])))))

(defn calc-points [card]
  (let [[winning-set card-numbers] card
        matches (filter #(contains? winning-set %) card-numbers)]
    (if (empty? matches)
      0
      ; calculate points according to the game rules where 1st match gives 1 pt
      ; then each match after that doubles the point value of a card
      (let [points (reduce (fn [acc _] (* 2 acc)) 1 (range (count matches)))]
        points))))

(defn solve [filename]
  (with-open [rdr (io/reader filename)]
    (->> (line-seq rdr)
         (map parse-card)
         (map calc-points)
         (reduce +))))

(println (solve "head.txt"))
