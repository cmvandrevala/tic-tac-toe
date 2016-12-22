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

  (it "returns empty if a cell has not been marked"
    (should= :empty (cell-status 5 empty-board)))

  (it "returns the player who has marked a cell"
    (let [board (mark :player_foo 5 empty-board)]
      (should= :player_foo (cell-status 5 board))))

  (it "returns the correct player out of two options"
    (let [board (mark :p2 6 (mark :p1 3 empty-board))]
      (should= :p2 (cell-status 6 board)))))
