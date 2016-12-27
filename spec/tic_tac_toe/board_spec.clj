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

(describe "a filled board"

  (it "returns false for an empty board"
      (should= false (filled? empty-board)))

  (it "returns false for a board with a few marks"
      (let [board (mark :p1 1 (mark :p2 2 (mark :p3 3 empty-board)))]
        (should= false (filled? board))))

  (it "returns false for a non-filled board, even if there is a winner"
      (let [board (mark :player-one 0 (mark :player-one 1 (mark :player-one 2 empty-board)))]
        (should= false (filled? board))))

  (it "returns true for a filled board with a winner"
      (let [board (mark :p 0 (mark :p 1 (mark :p 2 (mark :p 3 (mark :p 4 (mark :p 5 (mark :p 6 (mark :p 7 (mark :p 8 empty-board)))))))))]
        (should= true (filled? board))))

  (it "returns true for a filled board with no winner"
      (let [board (mark :p1 0 (mark :p2 1 (mark :p3 2 (mark :p4 3 (mark :p5 4 (mark :p6 5 (mark :p7 6 (mark :p8 7 (mark :p9 8 empty-board)))))))))]
        (should= true (filled? board)))))
