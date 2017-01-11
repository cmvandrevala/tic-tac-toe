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
        (should= 1 (utility :player-one current-board 7))))

  (it "scores a move as -1 if the opponent can win horizontally on the next turn"
      (let [current-board (b/mark-many :player-one [1 2])]
        (should= -1 (utility :player-two current-board 7))))

  (it "scores a move as -1 if the opponent can win vertically on the next turn"
      (let [current-board (b/mark-many :player-two [2 5])]
        (should= -1 (utility :player-one current-board 1))))

  (it "scores a move as -1 if the opponent can win diagonally on the next turn"
      (let [current-board (b/mark-many :player-two [0 4])]
        (should= -1 (utility :player-one current-board 1)))))

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

(describe "select best move"

  (it "selects a winning move from a list"
      (let [input [{0 nil} {1 nil} {2 nil} {3 nil} {4 nil} {5 1} {6 nil} {7 nil} {8 nil}]]
        (should= 5 (best-move input))))

  (it "selects a different winning move from a list"
      (let [input [{0 nil} {1 1} {2 nil} {3 nil} {4 nil} {5 nil} {6 nil} {7 nil} {8 nil}]]
        (should= 1 (best-move input))))

  (it "returns the latest winning move"
      (let [input [{0 nil} {1 1} {2 1} {3 1} {4 1} {5 nil} {6 nil} {7 1} {8 nil}]]
        (should= 7 (best-move input))))

  (it "ignores a tie if a win exists"
      (let [input [{0 0} {1 nil} {2 nil} {3 nil} {4 nil} {5 nil} {6 nil} {7 0} {8 1}]]
        (should= 8 (best-move input))))

  (it "ignores a loss if a win exists"
      (let [input [{0 nil} {1 nil} {2 1} {3 nil} {4 -1} {5 nil} {6 -1} {7 -1} {8 nil}]]
        (should= 2 (best-move input))))

  (it "selects a tie move from a list"
    (let [input [{0 nil} {1 nil} {2 nil} {3 nil} {4 nil} {5 nil} {6 0} {7 nil} {8 nil}]]
      (should= 6 (best-move input))))

  (it "ignores a loss if a tie exists"
      (let [input [{0 nil} {1 nil} {2 0} {3 nil} {4 -1} {5 nil} {6 -1} {7 -1} {8 nil}]]
        (should= 2 (best-move input))))

  (it "returns a space on the list, even if they are nil"
      (let [input [{1 nil} {3 nil} {4 nil} {5 nil} {6 nil} {7 nil}]]
        (should= 7 (best-move input))))

  (it "avoids a loss as much as possible"
      (let [input [{1 nil} {3 nil} {4 nil} {5 nil} {6 -1}]]
        (should= 5 (best-move input)))))

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
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "blocks an opponent who is about to win vertically"
      (let [current-board (b/mark-many :player-one [0 3])
            expected-output (b/mark :player-two 6 (b/mark-many :player-one [0 3]))]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "blocks an opponent who is about to win horizontally"
      (let [current-board (b/mark-many :player-two [6 8])
            expected-output (b/mark :player-one 7 (b/mark-many :player-two [6 8]))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "blocks an opponent who is about to win diagonally"
      (let [current-board (b/mark-many :player-two [4 6])
            expected-output (b/mark :player-one 2 (b/mark-many :player-two [4 6]))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "it is a fatalistic computer player"
      (let [current-board (b/mark-many :player-one [0 1 3])
            expected-output (b/mark :player-two 8 (b/mark-many :player-one [0 1 3]))]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "picks a winning spot if two spots are available"
      (let [current-board (b/mark-many :player-one [0 1 3])
            expected-output (b/mark :player-one 6 (b/mark-many :player-one [0 1 3]))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "does not pick a losing spot on a fixed board"
      (let [current-board (b/mark-many :player-one [0 1 3 4])
            expected-output (b/mark :player-one 8 (b/mark-many :player-one [0 1 3 4]))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "picks a winning spot over blocking a win (player one)"
      (let [current-board (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4]))
            expected-output (b/mark :player-one 5 (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4])))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "picks a winning spot over blocking a win (player two)"
      (let [current-board (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4]))
            expected-output (b/mark :player-two 8 (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4])))]
        (should= expected-output (unbeatable-ai :player-two current-board)))))
