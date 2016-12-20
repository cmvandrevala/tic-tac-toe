(ns tic-tac-toe.board)

(def minimum-cell-index 0)
(def maximum-cell-index 8)
(def empty-board [])

(defn in-range? [cell]
  (and (>= cell minimum-cell-index) (<= cell maximum-cell-index)))

(defn mark [player cell board]
  (if (in-range? cell)
    (conj board {player cell}) board))

(defn is-empty? [cell board]
  (let [filled-cells (into {} (map clojure.set/map-invert board))]
    (and
      (not= (get filled-cells cell) :player-one)
      (not= (get filled-cells cell) :player-two))))
