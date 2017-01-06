(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.board :as b]))

(defn return-board [player-one player-two board] board)

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

(describe "dumb computer"

  (it "fills in the first cell of an empty board"
      (should= [{:player-one 0}] (dumb-computer b/empty-board)))

  (it "fills in the first cell if it is available"
      (should= [{:player-one 5} {:player-two 0}] (dumb-computer (b/mark :player-one 5 b/empty-board)))))

(describe "the play loop"

  (it "performs the player-one action on a blank board"
      (should= [{:player-one 0}] (execute-play-loop return-board (partial move 0) (partial move 1) b/empty-board)))

  (it "performs the player-two action on a board with one move"
      (should= [{:player-one 0} {:player-two 1}] (execute-play-loop return-board (partial move 0) (partial move 1) (b/mark :player-one 0 b/empty-board)))))
