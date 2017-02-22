(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.board :as board]))

(defn return-board [player-one player-two board] board)

(describe "the current player"

  (it "is :player-one at the beginning of the game"
      (should= :player-one (current-player)))

  (it "is :player-two after one move"
      (let [board (board/mark :player-one 4 board/empty-board)]
        (should= :player-two (current-player board))))

  (it "is :player-one after two moves"
      (let [board (board/mark :player-two 1 (board/mark :player-one 7 board/empty-board))]
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

(describe "validating a move from the console"

  (it "does not allow an input less than zero"
    (let [board (move 5 (move 4 (move 3)))]
      (should= :integer-too-small (validate-move -2 board))))

  (it "does not allow an input greater than eight"
    (let [board (move 1 (move 4 (move 3)))]
      (should= :integer-too-large (validate-move 13 board))))

  (it "does not allow a decimal"
    (let [board (move 1 (move 4 (move 3)))]
      (should= :not-an-integer (validate-move 3.14159 board))))

  (it "does not allow a player to overwrite a space that is already taken"
    (let [board (move 1)]
      (should= :cell-taken (validate-move 1 board))))

  (it "rejects non-numeric strings as input"
    (let [board (move 3)]
      (should= :not-an-integer (validate-move "abcde" board))))

  (it "rejects a nil input"
    (should= :not-an-integer (validate-move nil board/empty-board)))

  (it "rejects a blank string input"
    (should= :not-an-integer (validate-move "" board/empty-board))))

(describe "convert a string to number"

  (it "converts a valid string input to a number"
      (should= 5 (string-to-number "5")))

  (it "converts an out-of-bounds input to a number"
      (should= 10 (string-to-number "10")))

  (it "returns a decimal number"
      (should= 12.5 (string-to-number "12.5")))

  (it "sets a string equal to nil"
      (should= nil (string-to-number "abcde")))

  (it "sets a blank string equal to nil"
      (should= nil (string-to-number ""))))

(describe "easy computer"

  (it "fills in the first cell of an empty board"
      (should= [{:player-one 0}] (easy-computer board/empty-board)))

  (it "fills in the first cell if it is available"
      (should= [{:player-one 5} {:player-two 0}] (easy-computer (board/mark :player-one 5 board/empty-board)))))

(describe "the play loop"

  (it "performs the player-one action on a blank board"
      (should= [{:player-one 0}] (execute-play-loop return-board (partial move 0) (partial move 1) board/empty-board)))

  (it "performs the player-two action on a board with one move"
      (should= [{:player-one 0} {:player-two 1}] (execute-play-loop return-board (partial move 0) (partial move 1) (board/mark :player-one 0 board/empty-board)))))
