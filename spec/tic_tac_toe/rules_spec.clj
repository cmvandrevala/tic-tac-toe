(ns tic-tac-toe.rules-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.rules :refer :all]
            [tic-tac-toe.board :as board]))

(defn- mark-three [player move1 move2 move3]
  (board/mark player move1
    (board/mark player move2
      (board/mark player move3 board/empty-board))))

(describe "game in progress"

  (it "returns true for an empty game"
    (should= true (game-in-progress? board/empty-board)))

  (it "returns true for a game in progress"
    (should= true (game-in-progress? (mark-three :player 1 5 8))))

  (it "returns false for a winner in the top row"
    (should= false (game-in-progress? (mark-three :player 0 1 2))))

  (it "returns false for a winner in the middle row"
    (should= false (game-in-progress? (mark-three :player 3 4 5))))

  (it "returns false for a winner in the bottom row"
    (should= false (game-in-progress? (mark-three :player 6 7 8)))))
