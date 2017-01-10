(ns tic-tac-toe.computer-ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-ai :refer :all]
            [tic-tac-toe.board :as b]))

(def almost-tie-game-board
    (b/mark :player-two 8
      (b/mark :player-two 6
        (b/mark-many :player-two [1 3 4]
          (b/mark-many :player-one [0 2 5])))))

(def complementary-almost-tie-game-board
    (b/mark :player-one 8
      (b/mark :player-one 6
        (b/mark-many :player-one [1 3 4]
          (b/mark-many :player-two [0 2 5])))))

(describe "a first available spot computer player"

  (it "takes index zero on an empty board when it is player one"
      (let [expected-output (b/mark :player-one 0 b/empty-board)]
        (should= expected-output (first-available-spot-ai :player-one b/empty-board))))

  (it "takes index zero on an empty board when it is player two"
      (let [expected-output (b/mark :player-two 0 b/empty-board)]
        (should= expected-output (first-available-spot-ai :player-two b/empty-board))))

  (it "takes index zero whenever it is available"
      (let [expected-output (b/mark :player-two 0 (b/mark :player-one 5 b/empty-board))
            starting-board (b/mark :player-one 5 b/empty-board)]
        (should= expected-output (first-available-spot-ai :player-two starting-board))))

  (it "takes index one if zero is unavailble"
      (let [expected-output (b/mark :player-two 1 (b/mark :player-one 0 b/empty-board))
            starting-board (b/mark :player-one 0 b/empty-board)]
        (should= expected-output (first-available-spot-ai :player-two starting-board))))

  (it "takes index two if zero and one are unavailble"
      (let [expected-output (b/mark :player-one 2 (b/mark :player-two 1 (b/mark :player-one 0 b/empty-board)))
            starting-board (b/mark :player-two 1 (b/mark :player-one 0 b/empty-board))]
        (should= expected-output (first-available-spot-ai :player-one starting-board))))

  (it "takes the first spot, even if it means a loss in the next turn"
      (let [expected-output (b/mark :player-two 1 (b/mark :player-one 3 (b/mark :player-one 0 b/empty-board)))
            starting-board (b/mark :player-one 3 (b/mark :player-one 0 b/empty-board))]
        (should= expected-output (first-available-spot-ai :player-two starting-board)))))

(describe "utility of a move in minimax"

  (it "scores a horizontal winning move in the top row with a +1"
      (let [current-board (b/mark-many :player-one [0 1])]
        (should= 1 (utility :player-one current-board 2))))

  (it "scores a horizontal winning move in the middle row with a +1"
      (let [current-board (b/mark-many :player-one [3 5])]
        (should= 1 (utility :player-one current-board 4))))

  (it "scores a horizontal winning move in the bottom row with a +1"
      (let [current-board (b/mark-many :player-two [7 8])]
        (should= 1 (utility :player-two current-board 6))))

  (it "scores a vertical winning move in the left column with a +1"
      (let [current-board (b/mark-many :player-two [0 3])]
        (should= 1 (utility :player-two current-board 6))))

  (it "scores a vertical winning move in the middle column with a +1"
      (let [current-board (b/mark-many :player-two [4 1])]
        (should= 1 (utility :player-two current-board 7))))

  (it "scores a vertical winning move in the right column with a +1"
      (let [current-board (b/mark-many :player-one [5 8])]
        (should= 1 (utility :player-one current-board 2))))

  (it "scores a left diagonal move with a +1"
      (let [current-board (b/mark-many :player-one [0 4])]
        (should= 1 (utility :player-one current-board 8))))

  (it "scores a right diagonal move with a +1"
      (let [current-board (b/mark-many :player-two [2 6])]
        (should= 1 (utility :player-two current-board 4))))

  (it "scores a tie move with a 0"
      (let [current-board almost-tie-game-board]
        (should= 0 (utility :player-one current-board 7))))

  (it "scores a tie move with a 0 (complementary board)"
      (let [current-board complementary-almost-tie-game-board]
        (should= 0 (utility :player-two current-board 7))))

  (it "scores a winning move on a full board with a +1"
      (let [current-board almost-tie-game-board]
        (should= 1 (utility :player-two current-board 7))))

  (it "scores a winning move on a full board with a +1 (complementary board)"
      (let [current-board complementary-almost-tie-game-board]
        (should= 1 (utility :player-one current-board 7)))))

(describe "utilities for remaining cells"

  (it "returns nine nils if the board is empty"
      (let [expected-output [{0 nil} {1 nil} {2 nil} {3 nil} {4 nil} {5 nil} {6 nil} {7 nil} {8 nil}]]
        (should= expected-output (utilities-for-remaining-cells :player-one b/empty-board))))

  (it "returns eight nils if the board has one move"
      (let [expected-output [{0 nil} {2 nil} {3 nil} {4 nil} {5 nil} {6 nil} {7 nil} {8 nil}]]
        (should= expected-output (utilities-for-remaining-cells :player-one (b/mark-many :player-one [1])))))

  (it "returns no entries if the board has no available moves"
        (should= [] (utilities-for-remaining-cells :player-one (b/mark :player-one 7 almost-tie-game-board))))

  (it "returns the correct output if the player can win in the top row"
      (let [expected-output [{2 1} {3 nil} {4 nil} {5 nil} {6 nil} {7 nil} {8 nil}]]
        (should= expected-output (utilities-for-remaining-cells :player-one (b/mark-many :player-one [0 1])))))

  (it "returns the correct output if the player can win in the left column"
      (let [expected-output [{1 nil} {2 nil} {3 1} {4 nil} {5 nil} {7 nil} {8 nil}]]
        (should= expected-output (utilities-for-remaining-cells :player-two (b/mark-many :player-two [6 0]))))))

(describe "an unbeatable computer player"

  (it "takes a tie move if it is the only move available"
      (let [current-board almost-tie-game-board
            expected-output (b/mark :player-one 7 almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a winning move if it is the only move available"
      (let [current-board almost-tie-game-board
            expected-output (b/mark :player-two 7 almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a tie move if it is the only move available (complementary board)"
      (let [current-board complementary-almost-tie-game-board
            expected-output (b/mark :player-two 7 complementary-almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a winning move if it is the only move available (complementary board)"
      (let [current-board complementary-almost-tie-game-board
            expected-output (b/mark :player-one 7 complementary-almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a horizontal winning move when it is available"
      (let [current-board (b/mark-many :player-one [0 1])
            expected-output (b/mark-many :player-one [0 1 2])]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a vertical winning move when it is available"
      (let [current-board (b/mark-many :player-two [1 4])
            expected-output (b/mark-many :player-two [1 4 7])]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a diagonal winning move when it is available"
      (let [current-board (b/mark-many :player-one [0 4])
            expected-output (b/mark-many :player-one [0 4 8])]
        (should= expected-output (unbeatable-ai :player-one current-board)))))
