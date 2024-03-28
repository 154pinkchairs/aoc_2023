(ns part1.core
  (:gen-class)
  (:import [java.io BufferedReader FileReader])
  (:require [clojure.string :as str]))

(defn read-schematic [filename]
  (try
    (with-open [rdr (-> filename FileReader. BufferedReader.)]
      (let [schematic (doall (line-seq rdr))]
        (println "Schematic:")
        (doseq [line schematic]
          (println line))
        (mapv #(str/split % #"") schematic))) ; Split each line into characters
    (catch Exception e
      (println "Error reading file:" (.getMessage e)))))

(defn digit? [c]
  ; check for null cell
  (and (not (nil? c))
       (Character/isDigit ^Character c)
       (re-matches #"\d+" c)))

(defn merge-digits [schematic]
  (mapv #(->> % (partition-by digit?) (mapv (apply str %)) (filter digit?)) schematic))

(defn is-part-number? [schematic x y]
  (let [height (count schematic)
        width (count (first schematic))
        directions [[-1 0] [1 0] [0 -1] [0 1] [-1 -1] [-1 1] [1 -1] [1 1]]
        in-range? (fn [x y] (and (>= x 0) (< x height) (>= y 0) (< y width)))
        valid-direction? (fn [[dx dy]]
                           (let [nx (+ x dx)
                                 ny (+ y dy)]
                             (and (in-range? nx ny)
                                  (not= (get-in schematic [nx ny]) \.)
                                  (digit? (get-in schematic [nx ny])))))]
    (println "Current cell:" x y "Value:" (get-in schematic [x y])) ; Debugging info
    (println "Adjacent cells:")
    (doseq [[dx dy] directions]
      (let [nx (+ x dx)
            ny (+ y dy)]
        (when (and (in-range? nx ny) (not= (get-in schematic [nx ny]) \.)))
        (println "  " nx ny "Value:" (get-in schematic [nx ny])))) ; Debugging info

    (and (digit? (get-in schematic [x y]))
         (some valid-direction? directions))))

(defn parse-schematic [filename]
  (let [schematic (read-schematic filename)]
    (if (nil? schematic)
      (println "Failed to read schematic. Exiting.")
      (let [height (count schematic)
            width (count (first schematic))]
        (println "Height:" height "Width:" width) ; Debugging info
        (reduce +
                (for [x (range height)
                      y (range width)
                      :when (is-part-number? schematic x y)]
                  (let [cell (get-in schematic [x y])]
                    (if (nil? cell)
                      (do (println "WARN: null cell at" x y) 0)
                      (Integer/parseInt cell)))))))))

(defn sum-part-numbers [filename]
  (let [schematic (read-schematic filename)]
    (if (nil? schematic)
      (println "Failed to read schematic. Exiting.")
      (let [part-numbers (merge-digits schematic)]
        (println "Part numbers:" part-numbers) ; Debugging info
        (reduce +
                (for [part-number part-numbers]
                  (Integer/parseInt part-number)))))))

(println (sum-part-numbers "input.txt"))
