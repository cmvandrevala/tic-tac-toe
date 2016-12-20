(ns tic-tac-toe.rules
  (:require [tic-tac-toe.board :as board]))

(defn- cells-filled [board cells]
  (and
    (=
      (board/cell-status (get cells 0) board)
      (board/cell-status (get cells 1) board)
      (board/cell-status (get cells 2) board))
    (not=
      (board/cell-status (get cells 0) board) :empty)))

(defn game-in-progress? [board]
  (and
    (not (cells-filled board [0 1 2]))
    (not (cells-filled board [3 4 5]))
    (not (cells-filled board [6 7 8]))))
