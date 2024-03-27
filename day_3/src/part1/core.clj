(ns part1.core
  (:gen-class)
  (:import [java.io BufferedReader FileReader]))

(defn read-schematic [^String filename]
  (with-open [rdr (BufferedReader. (FileReader. filename))]
    (doall (line-seq rdr))))

(defn is-part-number? [schematic symbols x y]
  (let [directions [[-1 0] [1 0] [0 -1] [0 1] [-1 -1] [-1 1] [1 -1] [1 1]]
        height (count schematic)
        width (count (first schematic))
        in-range? (fn [x y] (and (<= 0 x (dec height)) (<= 0 y (dec width))))
        cell (nth (nth schematic x) y)]
    (and cell
         (Character/isDigit ^Character cell)
         (some #(let [[dx dy] %
                      [nx ny] [(+ x dx) (+ y dy)]
                      adjacent-cell (if (in-range? nx ny) (nth (nth schematic nx) ny 0) nil)]
                  (and adjacent-cell
                       (contains? symbols adjacent-cell)))
               directions))))

(defn sum-part-numbers [^String filename]
  (let [schematic (read-schematic filename)
        symbols (set (filter #(not (or (Character/isDigit ^Character %) (= \. ^Character %)))
                             (apply concat schematic)))
        ; declare pairs of all 8 possible directions on a 2D grid
        height (count schematic)
        width (count (first schematic))]
    (reduce + (for [x (range height)
                    y (range width)
                    :when (is-part-number? schematic symbols x y)]
                (- (int (get (get schematic x) y \0)) 48)))))

(println (sum-part-numbers "input.txt"))
