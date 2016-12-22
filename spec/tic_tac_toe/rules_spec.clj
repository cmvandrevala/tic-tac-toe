(ns tic-tac-toe.rules-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.rules :refer :all]
            [tic-tac-toe.board :as board]))

(defn- mark-many
  ([player moves] (mark-many player moves board/empty-board))
  ([player moves game-board] (if (empty? moves) game-board (mark-many player (rest moves) (board/mark player (first moves) game-board)))))

(describe "game in progress"

  (it "returns true for an empty game"
    (should= true (game-in-progress? board/empty-board)))

  (it "returns true for a game in progress"
    (should= true (game-in-progress? (mark-many :player [1 5 8]))))

  (it "returns false for a winner in the top row"
    (should= false (game-in-progress? (mark-many :player [0 1 2]))))

  (it "returns false for a winner in the middle row"
    (should= false (game-in-progress? (mark-many :player [3 4 5]))))

  (it "returns false for a winner in the bottom row"
    (should= false (game-in-progress? (mark-many :player [6 7 8]))))

  (it "returns false for a winner in the left column"
    (should= false (game-in-progress? (mark-many :player-one [0 3 6]))))

  (it "returns false for a winner in the middle column"
    (should= false (game-in-progress? (mark-many :player-two [1 4 7]))))

  (it "returns false for a winner in the right column"
    (should= false (game-in-progress? (mark-many :player-foo [2 5 8]))))

  (it "returns false for a winner in the left diagonal"
    (should= false (game-in-progress? (mark-many :player-bar [0 4 8]))))

  (it "returns false for a winner in the right diagonal"
    (should= false (game-in-progress? (mark-many :player-baz [2 4 6]))))

  (it "returns false for a tie game"
    (let [full-board (mark-many :b [1 4 5 6] (mark-many :a [0 2 3 7 8]))]
      (should= false (game-in-progress? full-board)))))
