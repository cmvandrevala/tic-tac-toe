(ns tic-tac-toe.computer-ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-ai :refer :all]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.game-tree :as game-tree]))

(def almost-tie-game-board
    (board/mark :player-two 2
      (board/mark :player-two 0
        (board/mark-many :player-two [7 3 4]
          (board/mark-many :player-one [6 8 5])))))

(def complementary-almost-tie-game-board
    (board/mark :player-one 8
      (board/mark :player-one 6
        (board/mark-many :player-one [1 3 4]
          (board/mark-many :player-two [0 2 5])))))

(describe "a first available spot computer player"

  (it "takes index zero on an empty board when it is player one"
      (let [expected-output (board/mark :player-one 0 board/empty-board)]
        (should= expected-output (first-available-spot-ai :player-one board/empty-board))))

  (it "takes index zero on an empty board when it is player two"
      (let [expected-output (board/mark :player-two 0 board/empty-board)]
        (should= expected-output (first-available-spot-ai :player-two board/empty-board))))

  (it "takes index zero whenever it is available"
      (let [expected-output (board/mark :player-two 0 (board/mark :player-one 5 board/empty-board))
            starting-board (board/mark :player-one 5 board/empty-board)]
        (should= expected-output (first-available-spot-ai :player-two starting-board))))

  (it "takes index one if zero is unavailble"
      (let [expected-output (board/mark :player-two 1 (board/mark :player-one 0 board/empty-board))
            starting-board (board/mark :player-one 0 board/empty-board)]
        (should= expected-output (first-available-spot-ai :player-two starting-board))))

  (it "takes index two if zero and one are unavailble"
      (let [expected-output (board/mark :player-one 2 (board/mark :player-two 1 (board/mark :player-one 0 board/empty-board)))
            starting-board (board/mark :player-two 1 (board/mark :player-one 0 board/empty-board))]
        (should= expected-output (first-available-spot-ai :player-one starting-board))))

  (it "takes the first spot, even if it means a loss in the next turn"
      (let [expected-output (board/mark :player-two 1 (board/mark :player-one 3 (board/mark :player-one 0 board/empty-board)))
            starting-board (board/mark :player-one 3 (board/mark :player-one 0 board/empty-board))]
        (should= expected-output (first-available-spot-ai :player-two starting-board)))))

(describe "the utility function for minimax"

  (it "returns nil for a game in progress"
      (should= nil (utility :player-one board/empty-board)))

  (it "returns 1 when the player wins"
      (let [current-board (board/mark-many :player-one [0 1 2])]
        (should= 1 (utility :player-one current-board))))

  (it "returns -1 when the opponent wins"
      (let [current-board (board/mark-many :player-two [1 4 7])]
        (should= -1 (utility :player-one current-board))))

  (it "returns 0 for a tie"
      (let [current-board (board/mark :player-one 1 almost-tie-game-board)]
        (should= 0 (utility :player-two current-board)))))

(describe "generating a game tree"

  (it "returns an empty hash-map if the board is full"
    (should= {:value nil} (generate-game-tree :player-one (board/mark-many :player-one (range 9)))))

  (it "returns a single node if the board is almost full (player one ties)"
    (should= {:value nil :children [{:player :player-one :cell 1 :value 0}]} (generate-game-tree :player-one almost-tie-game-board)))

  (it "returns a single node if the board is almost full (player two wins)"
    (should= {:value nil :children [{:player :player-two :cell 1 :value 1}]} (generate-game-tree :player-two almost-tie-game-board)))

  (it "returns a single node if the board is almost full (player two ties)"
    (should= {:value nil :children [{:player :player-two :cell 7 :value 0}]} (generate-game-tree :player-two complementary-almost-tie-game-board)))

  (it "returns a single node if the board is almost full (player one wins)"
    (should= {:value nil :children [{:player :player-two :cell 7 :value 0}]} (generate-game-tree :player-two complementary-almost-tie-game-board)))

  (it "builds an appropriate tree for two open cells (player two's turn)"
    (let [current-board (board/mark-many :player-two [2 3 8] (board/mark-many :player-one [0 4 5 7]))
          expected-output {:value nil :children [{:player :player-two :cell 6 :value nil :children [{:player :player-one :cell 1 :value -1}]} {:player :player-two :cell 1 :value nil :children [{:player :player-one :cell 6 :value 0}]}]}]
      (should= expected-output (generate-game-tree :player-two current-board))))

  (it "builds an appropriate tree for three open cells (player one's turn)"
    (let [current-board (board/mark-many :player-two [1 4 8] (board/mark-many :player-one [2 3 5]))
          d1 (game-tree/node {:player :player-one :cell 7 :value 0})
          d2 (game-tree/node {:player :player-one :cell 0 :value 0})
          c1 (game-tree/node {:player :player-two :cell 6 :value nil})
          c2 (game-tree/node {:player :player-two :cell 7 :value -1})
          c3 (game-tree/node {:player :player-two :cell 0 :value -1})
          c4 (game-tree/node {:player :player-two :cell 7 :value -1})
          c5 (game-tree/node {:player :player-two :cell 0 :value -1})
          c6 (game-tree/node {:player :player-two :cell 6 :value nil})
          b1 (game-tree/node {:player :player-one :cell 0 :value nil})
          b2 (game-tree/node {:player :player-one :cell 6 :value nil})
          b3 (game-tree/node {:player :player-one :cell 7 :value nil})
          c1c (game-tree/add-child d1 c1)
          c6c (game-tree/add-child d2 c6)
          b1c (game-tree/add-child c2 (game-tree/add-child c1c b1))
          b2c (game-tree/add-child c4 (game-tree/add-child c3 b2))
          b3c (game-tree/add-child c6c (game-tree/add-child c5 b3))
          expected-output (game-tree/add-child b3c (game-tree/add-child b2c (game-tree/add-child b1c game-tree/nil-node)))]
      (should= expected-output (generate-game-tree :player-one current-board)))))

(describe "scoring some sample game trees"

  (it "four open cells variation 1"
    (let [current-board (board/mark-many :player-two [1 4] (board/mark-many :player-one [0 2 7]))]
      (should= 0 (game-tree/score (generate-game-tree :player-two current-board)))))

  (it "four open cells variation 2"
    (let [current-board (board/mark-many :player-two [2 3] (board/mark-many :player-one [0 6 8]))]
      (should= -1 (game-tree/score (generate-game-tree :player-two current-board)))))

  (it "four open cells variation 3"
    (let [current-board (board/mark-many :player-two [1 5] (board/mark-many :player-one [2 4 8]))]
      (should= -1 (game-tree/score (generate-game-tree :player-two current-board)))))

  (it "four open cells variation 4"
    (let [current-board (board/mark-many :player-two [2 8] (board/mark-many :player-one [0 5 7]))]
      (should= 0 (game-tree/score (generate-game-tree :player-two current-board)))))

  (it "five open cells variation 1"
    (let [current-board (board/mark-many :player-two [2 8] (board/mark-many :player-one [0 5]))]
      (should= 1 (game-tree/score (generate-game-tree :player-one current-board)))))

  (it "five open cells variation 2"
    (let [current-board (board/mark-many :player-two [3 4] (board/mark-many :player-one [0 8]))]
      (should= 0 (game-tree/score (generate-game-tree :player-one current-board)))))

  (it "five open cells variation 3"
    (let [current-board (board/mark-many :player-two [0 7] (board/mark-many :player-one [2 3]))]
      (should= 1 (game-tree/score (generate-game-tree :player-one current-board)))))

  (it "six open cells variation 1"
    (let [current-board (board/mark :player-two 0 (board/mark-many :player-one [1 2]))]
      (should= 1 (game-tree/score (generate-game-tree :player-one current-board)))))

  (it "seven, fixed, open cells (player 1)"
    (let [current-board (board/mark-many :player-one [1 2])]
      (should= 1 (game-tree/score (generate-game-tree :player-one current-board)))))

  (it "seven, fixed, open cells (player 2)"
    (let [current-board (board/mark-many :player-one [1 2])]
      (should= -1 (game-tree/score (generate-game-tree :player-two current-board))))))

(describe "the minimax calculation"

  (it "scores a horizontal winning move in the top row with a +1"
      (let [current-board (board/mark-many :player-one [0 1])]
        (should= 1 (minimax :player-one 2 current-board))))

  (it "scores a horizontal winning move in the middle row with a +1"
      (let [current-board (board/mark-many :player-one [3 5])]
        (should= 1 (minimax :player-one 4 current-board))))

  (it "scores a horizontal winning move in the bottom row with a +1"
      (let [current-board (board/mark-many :player-two [7 8])]
        (should= 1 (minimax :player-two 6 current-board))))

  (it "scores a vertical winning move in the left column with a +1"
      (let [current-board (board/mark-many :player-two [0 3])]
        (should= 1 (minimax :player-two 6 current-board))))

  (it "scores a vertical winning move in the middle column with a +1"
      (let [current-board (board/mark-many :player-two [4 1])]
        (should= 1 (minimax :player-two 7 current-board))))

  (it "scores a vertical winning move in the right column with a +1"
      (let [current-board (board/mark-many :player-one [5 8])]
        (should= 1 (minimax :player-one 2 current-board))))

  (it "scores a left diagonal move with a +1"
      (let [current-board (board/mark-many :player-one [0 4])]
        (should= 1 (minimax :player-one 8 current-board))))

  (it "scores a right diagonal move with a +1"
      (let [current-board (board/mark-many :player-two [2 6])]
        (should= 1 (minimax :player-two 4 current-board))))

  (it "scores a tie move with a 0"
      (let [current-board almost-tie-game-board]
        (should= 0 (minimax :player-one 1 current-board))))

  (it "scores a tie move with a 0 (complementary board)"
      (let [current-board complementary-almost-tie-game-board]
        (should= 0 (minimax :player-two 7 current-board))))

  (it "scores a winning move on a full board with a +1"
      (let [current-board almost-tie-game-board]
        (should= 1 (minimax :player-two 1 current-board))))

  (it "scores a winning move on a full board with a +1 (complementary board)"
      (let [current-board complementary-almost-tie-game-board]
        (should= 1 (minimax :player-one 7 current-board))))

  (it "scores a move as -1 if the opponent can win horizontally on the next turn"
      (let [current-board (board/mark-many :player-one [1 2])]
        (should= -1 (minimax :player-two 7 current-board))))

  (it "scores a move as -1 if the opponent can win vertically on the next turn"
      (let [current-board (board/mark-many :player-two [0 3])]
        (should= -1 (minimax :player-one 1 current-board))))

  (it "scores a move as -1 if the opponent can win diagonally on the next turn"
      (let [current-board (board/mark-many :player-two [2 4])]
        (should= -1 (minimax :player-one 1 current-board)))))

(describe "select best move"

  (it "selects a winning move from a list"
      (let [input [{0 -1} {1 0} {2 -1} {3 -1} {4 -1} {5 1} {6 -1} {7 -1} {8 0}]]
        (should= 5 (best-move input))))

  (it "selects a winning move from an unordered list"
      (let [input [{1 0} {4 -1} {2 -1} {5 1} {6 -1} {7 -1} {0 -1} {8 0} {3 -1}]]
        (should= 5 (best-move input))))

  (it "selects a different winning move from a list"
      (let [input [{0 -1} {1 1} {2 0} {3 -1} {4 -1} {5 0} {6 -1} {7 -1} {8 -1}]]
        (should= 1 (best-move input))))

  (it "returns the latest winning move"
      (let [input [{0 -1} {1 1} {2 1} {3 1} {4 1} {5 -1} {6 1} {7 1} {8 -1}]]
        (should= 7 (best-move input))))

  (it "selects a tie move from a list"
    (let [input [{0 -1} {1 -1} {2 0} {3 -1} {4 -1} {5 -1} {6 0} {7 -1} {8 -1}]]
      (should= 6 (best-move input))))

  (it "selects a different tie from a list"
      (let [input [{0 -1} {1 -1} {2 0} {3 -1} {4 -1} {5 -1} {6 -1} {7 -1} {8 -1}]]
        (should= 2 (best-move input))))

  (it "returns a loss if there are no other choices"
      (let [input [{0 -1} {1 -1} {2 -1} {3 -1} {4 -1} {5 -1} {6 -1} {7 -1} {8 -1}]]
        (should= 8 (best-move input)))))

(describe "an unbeatable computer player"

  (it "always starts the game off in the upper left square if it is the first player"
      (should= (board/mark :player-one 0 board/empty-board) (unbeatable-ai :player-one board/empty-board)))

  (it "takes the center spot if it is available on the second move"
      (let [current-board (board/mark :player-one 5 board/empty-board)
            expected-output (board/mark :player-two 4 (board/mark :player-one 5 board/empty-board))]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes the upper left hand corner if the center spot is taken on the second move"
      (let [current-board (board/mark :player-one 4 board/empty-board)
            expected-output (board/mark :player-two 0 (board/mark :player-one 4 board/empty-board))]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a tie move if it is the only move available"
      (let [current-board almost-tie-game-board
            expected-output (board/mark :player-one 1 almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a winning move if it is the only move available"
      (let [current-board almost-tie-game-board
            expected-output (board/mark :player-two 1 almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a tie move if it is the only move available (complementary board)"
      (let [current-board complementary-almost-tie-game-board
            expected-output (board/mark :player-two 7 complementary-almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a winning move if it is the only move available (complementary board)"
      (let [current-board complementary-almost-tie-game-board
            expected-output (board/mark :player-one 7 complementary-almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "it is a fatalistic computer player"
      (let [current-board (board/mark-many :player-one [0 1 3])
            expected-output (board/mark :player-two 8 (board/mark-many :player-one [0 1 3]))]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "properly moves on a board with a win and a tie"
      (let [current-board (board/mark-many :player-two [1 4] (board/mark-many :player-one [0 2 5]))
            expected-output (board/mark-many :player-two [1 4 7] (board/mark-many :player-one [0 2 5]))]
        (should= expected-output (unbeatable-ai :player-two current-board)))))
