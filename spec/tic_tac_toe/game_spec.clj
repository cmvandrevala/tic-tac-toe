(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.board :as b]))

(describe "the current player"

  (it "is :player-one at the beginning of the game"
      (should= :player-one (current-player)))

  (it "is :player-two after one move"
      (let [board (b/mark :player-one 4 b/empty-board)]
        (should= :player-two (current-player board))))

  (it "is :player-one after two moves"
      (let [board (b/mark :player-two 1 (b/mark :player-one 7 b/empty-board))]
        (should= :player-one (current-player board)))))

(describe "a move"

  (it "makes the first move"
    (let [board (move 0)]
      (should= [{:player-one 0}] board)))

  (it "makes a second move"
    (let [board (move 1 (move 3))]
      (should= [{:player-one 3} {:player-two 1}] board)))

  (it "makes a third move"
    (let [board (move 2 (move 6 (move 1)))]
      (should= [{:player-one 1} {:player-two 6} {:player-one 2}] board)))

  (it "makes many moves"
    (let [board (move 5 (move 4 (move 6 (move 3))))]
      (should= [{:player-one 3} {:player-two 6} {:player-one 4} {:player-two 5}] board))))

(describe "convert a string to number"

  (it "converts a valid string input to a number"
      (should= 5 (string-to-number "5")))

  (it "converts an out-of-bounds input to a number"
      (should= 10 (string-to-number "10")))

  (it "converts a decimal value to an integer via the floor function"
      (should= 12 (string-to-number "12.5")))

  (it "sets a string equal to an out-of-bounds integer"
      (should= 10 (string-to-number "abcde"))))
