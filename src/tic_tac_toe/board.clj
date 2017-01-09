(ns tic-tac-toe.board
  (:require clojure.set))

(def minimum-cell-index 0)
(def maximum-cell-index 8)
(def total-number-of-cells 9)
(def empty-board [])

(defn- in-range? [cell]
  (and (>= cell minimum-cell-index) (<= cell maximum-cell-index)))

(defn mark [player cell board]
  (if (in-range? cell)
    (conj board {player cell}) board))

(defn cell-status [cell board]
  (let [filled-cells (into {} (map clojure.set/map-invert board))]
    (if (get filled-cells cell) (get filled-cells cell) :empty)))

(defn filled? [board]
    (let [filled-cells (into {} (map clojure.set/map-invert board))]
      (= (count (set (keys filled-cells))) total-number-of-cells)))

(defn remaining-spaces [board]
  (filter #(= :empty (cell-status % board)) (range total-number-of-cells)))

(def horizontal-bar "\n-----------\n")

(defn- formatted-mark [cell board]
  (let [status (cell-status cell board)]
    (case status
      :player-one " X "
      :player-two " O "
      (str " " cell " "))))

(defn current-board
  ([] (current-board empty-board))
  ([board] (str (formatted-mark 0 board) "|" (formatted-mark 1 board) "|" (formatted-mark 2 board) horizontal-bar
                (formatted-mark 3 board) "|" (formatted-mark 4 board) "|" (formatted-mark 5 board) horizontal-bar
                (formatted-mark 6 board) "|" (formatted-mark 7 board) "|" (formatted-mark 8 board) "\n")))

(defn mark-many
  ([player moves] (mark-many player moves empty-board))
  ([player moves game-board] (if (empty? moves) game-board (mark-many player (rest moves) (mark player (first moves) game-board)))))
