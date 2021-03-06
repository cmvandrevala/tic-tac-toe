(ns tic-tac-toe.rules-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.rules :refer :all]
            [tic-tac-toe.board :as board]))

(def tie-game-board
  (board/mark :player-one 7
    (board/mark :player-two 8
      (board/mark :player-two 6
        (board/mark-many :player-two [1 3 4]
          (board/mark-many :player-one [0 2 5]))))))

(describe "game in progress"

  (it "returns true for an empty game"
    (should= true (game-in-progress? board/empty-board)))

  (it "returns true for a game in progress"
    (should= true (game-in-progress? (board/mark-many :player [1 5 8]))))

  (it "returns false for a winner in the top row"
    (should= false (game-in-progress? (board/mark-many :player [0 1 2]))))

  (it "returns false for a winner in the middle row"
    (should= false (game-in-progress? (board/mark-many :player [3 4 5]))))

  (it "returns false for a winner in the bottom row"
    (should= false (game-in-progress? (board/mark-many :player [6 7 8]))))

  (it "returns false for a winner in the left column"
    (should= false (game-in-progress? (board/mark-many :player-one [0 3 6]))))

  (it "returns false for a winner in the middle column"
    (should= false (game-in-progress? (board/mark-many :player-two [1 4 7]))))

  (it "returns false for a winner in the right column"
    (should= false (game-in-progress? (board/mark-many :player-foo [2 5 8]))))

  (it "returns false for a winner in the left diagonal"
    (should= false (game-in-progress? (board/mark-many :player-bar [0 4 8]))))

  (it "returns false for a winner in the right diagonal"
    (should= false (game-in-progress? (board/mark-many :player-baz [2 4 6]))))

  (it "returns false for a tie game"
    (let [full-board (board/mark-many :b [1 4 5 6] (board/mark-many :a [0 2 3 7 8]))]
      (should= false (game-in-progress? full-board)))))

(describe "game status"

  (it "returns :in-progress for an empty board"
    (should= :in-progress (game-status board/empty-board)))

  (it "returns :in-progress for a partially filled board"
    (let [board (board/mark-many :player-one [1 2 7])]
      (should= :in-progress (game-status board))))

  (it "returns :player-one if player one wins horizontally"
    (let [board (board/mark-many :player-one [0 1 2])]
      (should= :player-one (game-status board))))

  (it "returns :player-one if player one wins vertically"
    (let [board (board/mark-many :player-one [4 1 7])]
      (should= :player-one (game-status board))))

  (it "returns :player-two if player two wins vertically"
    (let [board (board/mark-many :player-two [2 5 8])]
      (should= :player-two (game-status board))))

  (it "returns :tie if the game is a tie"
    (should= :tie (game-status tie-game-board))))
