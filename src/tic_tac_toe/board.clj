(ns tic-tac-toe.board
  (:require clojure.set))

(def minimum-cell-index 0)
(def maximum-cell-index 8)
(def empty-board [])

(defn- in-range? [cell]
  (and (>= cell minimum-cell-index) (<= cell maximum-cell-index)))

(defn mark [player cell board]
  (if (in-range? cell)
    (conj board {player cell}) board))

(defn cell-status [cell board]
  (let [filled-cells (into {} (map clojure.set/map-invert board))]
    (if (get filled-cells cell) (get filled-cells cell) :empty)))
