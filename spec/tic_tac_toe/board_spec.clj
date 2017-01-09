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
      (let [board (mark-many :player-one [0 1 2])]
        (should= false (filled? board))))

  (it "returns true for a filled board with a winner"
      (let [board (mark-many :p [0 1 2 3 4 5 6 7 8])]
        (should= true (filled? board))))

  (it "returns true for a filled board with no winner"
      (let [board (mark :p1 0 (mark :p2 1 (mark :p3 2 (mark :p4 3 (mark :p5 4 (mark :p6 5 (mark :p7 6 (mark :p8 7 (mark :p9 8 empty-board)))))))))]
        (should= true (filled? board)))))

(describe "formatted marks on the board"

  (it "returns an empty board if there are no marks"
      (should= (str " 0 | 1 | 2 \n"
                    "-----------\n"
                    " 3 | 4 | 5 \n"
                    "-----------\n"
                    " 6 | 7 | 8 \n") (current-board)))

  (it "returns a board with a mark for player one"
      (let [board (mark :player-one 1 empty-board)]
        (should= (str " 0 | X | 2 \n"
                      "-----------\n"
                      " 3 | 4 | 5 \n"
                      "-----------\n"
                      " 6 | 7 | 8 \n") (current-board board))))

  (it "returns a board with a mark for player two"
      (let [board (mark :player-two 7 empty-board)]
        (should= (str " 0 | 1 | 2 \n"
                      "-----------\n"
                      " 3 | 4 | 5 \n"
                      "-----------\n"
                      " 6 | O | 8 \n") (current-board board))))

  (it "returns a board with two marks"
      (let [board (mark :player-one 5 (mark :player-two 0 empty-board))]
        (should= (str " O | 1 | 2 \n"
                      "-----------\n"
                      " 3 | 4 | X \n"
                      "-----------\n"
                      " 6 | 7 | 8 \n") (current-board board))))

  (it "returns a board with many marks"
      (let [board (mark :player-one 1 (mark :player-two 8 (mark :player-one 5 (mark :player-two 0 empty-board))))]
        (should= (str " O | X | 2 \n"
                      "-----------\n"
                      " 3 | 4 | X \n"
                      "-----------\n"
                      " 6 | 7 | O \n") (current-board board)))))

(describe "remaining spaces on the board"

  (it "returns all of the cells if no moves have been made"
      (should= [0 1 2 3 4 5 6 7 8] (remaining-spaces empty-board)))

  (it "returns the remaining cells if the first one has been marked"
      (should= [1 2 3 4 5 6 7 8] (remaining-spaces (mark :player-one 0 empty-board))))

  (it "returns the remaining cells if a random cell has been marked"
      (should= [0 1 2 3 5 6 7 8] (remaining-spaces (mark :player-two 4 empty-board))))

  (it "returns the remaining cells if two have been marked"
      (should= [0 1 2 3 5 6 7] (remaining-spaces (mark :b 8 (mark :a 4 empty-board)))))

  (it "returns the remaining cells if many have been marked"
      (should= [1 2 3 5 7] (remaining-spaces (mark :b 6 (mark :a 0 (mark :b 8 (mark :a 4 empty-board))))))))

(describe "marking many cells at once"

  (it "can mark one cell"
      (should= [{:player-one 2}] (mark-many :player-one [2])))

  (it "can mark two cells"
      (should= [{:p2 8} {:p2 7}] (mark-many :p2 [8 7])))

  (it "can mark three cells"
      (should= [{:p1 5} {:p1 6} {:p1 7}] (mark-many :p1 [5 6 7])))

  (it "can be passed a board"
      (should= [{:p1 5} {:p1 6} {:p1 7} {:p2 4} {:p2 3} ] (mark-many :p2 [4 3] (mark-many :p1 [5 6 7])))))
