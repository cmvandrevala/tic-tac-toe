(ns tic-tac-toe.board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :refer :all]))

(describe "current marks on the game board"

  (it "returns an empty board"
    (should= [] empty-board))

  (it "marks the first space"
    (let [board (mark :player-one 0 empty-board)]
      (should= [{:player-one 0}] board)))

  (it "marks two spaces"
    (let [board (mark :p2 5 (mark :p1 0 empty-board))]
      (should= [{:p1 0} {:p2 5}] board)))

  (it "ignores negative cells"
    (let [board (mark :p1 -10 empty-board)]
      (should= empty-board board)))

  (it "ignores cells with an index greater than 8"
    (let [board (mark :foo 9 empty-board)]
      (should= empty-board board)))

  (it "informs a user if a cell is empty"
    (should= true (is-empty? 5 empty-board)))

  (it "informs a user if a cell is filled"
    (let [board (mark :player-two 3 (mark :player-one 1 empty-board))]
      (should= false (is-empty? 1 board)))))
