(ns part1.test.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [part1.core :as core]))
; input is something like:
; "..172..............................454..46.......507..........809......923.778..................793..............137.............238........" 
; "............*.........712........=.......*................515.*...........*.......690.........../..........658.........=.........*.........."
;  ".........823.835........%.........710.....749........134..%............................#812...&.....925.../..........276.......386.........."
;  test if we can exfiltrate the numbers, then do a filter-map-reduce by symbol adjacency on them
;  the sum-part-numbers MUST NOT return 0
(deftest sum-part-numbers
  (testing "sum-part-numbers"
    (is (not= 0 (core/sum-part-numbers "input.txt")))))
