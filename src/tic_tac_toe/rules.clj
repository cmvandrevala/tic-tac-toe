(ns tic-tac-toe.rules
  (:require [tic-tac-toe.board :as b]))

(def winning-combinations [[0 1 2] [3 4 5] [6 7 8]
                            [0 3 6] [1 4 7] [2 5 8]
                            [0 4 8] [2 4 6]])

(defn- lazy-contains? [col key]
  (some #{key} col))

(defn- statuses [board cells]
  (map b/cell-status cells (repeat 3 board)))

(defn- cells-filled [board cells]
  (let [statuses (map b/cell-status cells (repeat 3 board))]
    (and
      (= (count (set statuses)) 1)
      (not= (b/cell-status (get cells 0) board) :empty))))

(defn game-in-progress? [board]
  (let [cells-filled-on-board (partial cells-filled board)]
    (and
      (= (set (map cells-filled-on-board winning-combinations)) #{false})
      (not (b/filled? board)))))

(defn game-status [board]
  (let [board-statuses (partial statuses board)
        cell-values (map set (map board-statuses winning-combinations))]
    (if (lazy-contains? cell-values #{:player-one})
      :player-one-wins
      (if (lazy-contains? cell-values #{:player-two})
        :player-two-wins
        (if (game-in-progress? board)
          :in-progress
          :tie)))))
