(ns tic-tac-toe.computer-ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-ai :refer :all]
            [tic-tac-toe.board :as b]
            [tic-tac-toe.game-tree :as gt]))

(def almost-tie-game-board
    (b/mark :player-two 2
      (b/mark :player-two 0
        (b/mark-many :player-two [7 3 4]
          (b/mark-many :player-one [6 8 5])))))

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

(describe "the utility function for minimax"

  (it "returns nil for a game in progress"
      (should= nil (utility :player-one b/empty-board)))

  (it "returns 1 when the player wins"
      (let [current-board (b/mark-many :player-one [0 1 2])]
        (should= 1 (utility :player-one current-board))))

  (it "returns -1 when the opponent wins"
      (let [current-board (b/mark-many :player-two [1 4 7])]
        (should= -1 (utility :player-one current-board))))

  (it "returns 0 for a tie"
      (let [current-board (b/mark :player-one 1 almost-tie-game-board)]
        (should= 0 (utility :player-two current-board)))))

(describe "generating a game tree"

  (it "returns an empty hash-map if the board is full"
    (should= {:value nil} (generate-game-tree :player-one (b/mark-many :p (range 9)))))

  (it "returns a single node if the board is almost full (player one ties)"
    (should= {:value nil :children [{:player :player-one :cell 1 :value 0}]} (generate-game-tree :player-one almost-tie-game-board)))

  (it "returns a single node if the board is almost full (player two wins)"
    (should= {:value nil :children [{:player :player-two :cell 1 :value 1}]} (generate-game-tree :player-two almost-tie-game-board)))

  (it "returns a single node if the board is almost full (player two ties)"
    (should= {:value nil :children [{:player :player-two :cell 7 :value 0}]} (generate-game-tree :player-two complementary-almost-tie-game-board)))

  (it "returns a single node if the board is almost full (player one wins)"
    (should= {:value nil :children [{:player :player-two :cell 7 :value 0}]} (generate-game-tree :player-two complementary-almost-tie-game-board)))

  (it "builds an appropriate tree for two open cells (player two's turn)"
    (let [current-board (b/mark-many :player-two [2 3 8] (b/mark-many :player-one [0 4 5 7]))
          expected-output {:value nil :children [{:player :player-two :cell 6 :value nil :children [{:player :player-one :cell 1 :value -1}]} {:player :player-two :cell 1 :value nil :children [{:player :player-one :cell 6 :value 0}]}]}]
      (should= expected-output (generate-game-tree :player-two current-board))))

  (it "builds an appropriate tree for three open cells (player one's turn)"
    (let [current-board (b/mark-many :player-two [1 4 8] (b/mark-many :player-one [2 3 5]))
          d1 (gt/node {:player :player-one :cell 7 :value 0})
          d2 (gt/node {:player :player-one :cell 0 :value 0})
          c1 (gt/node {:player :player-two :cell 6 :value nil})
          c2 (gt/node {:player :player-two :cell 7 :value -1})
          c3 (gt/node {:player :player-two :cell 0 :value -1})
          c4 (gt/node {:player :player-two :cell 7 :value -1})
          c5 (gt/node {:player :player-two :cell 0 :value -1})
          c6 (gt/node {:player :player-two :cell 6 :value nil})
          b1 (gt/node {:player :player-one :cell 0 :value nil})
          b2 (gt/node {:player :player-one :cell 6 :value nil})
          b3 (gt/node {:player :player-one :cell 7 :value nil})
          c1c (gt/add-child d1 c1)
          c6c (gt/add-child d2 c6)
          b1c (gt/add-child c2 (gt/add-child c1c b1))
          b2c (gt/add-child c4 (gt/add-child c3 b2))
          b3c (gt/add-child c6c (gt/add-child c5 b3))
          expected-output (gt/add-child b3c (gt/add-child b2c (gt/add-child b1c gt/nil-node)))]
      (should= expected-output (generate-game-tree :player-one current-board)))))

(describe "utility of a move in minimax"

  (it "scores a horizontal winning move in the top row with a +1"
      (let [current-board (b/mark-many :player-one [0 1])]
        (should= 1 (minimax :player-one :player-two current-board 2))))

  (it "scores a horizontal winning move in the middle row with a +1"
      (let [current-board (b/mark-many :player-one [3 5])]
        (should= 1 (minimax :player-one :player-two current-board 4))))

  (it "scores a horizontal winning move in the bottom row with a +1"
      (let [current-board (b/mark-many :player-two [7 8])]
        (should= 1 (minimax :player-two :player-one current-board 6))))

  (it "scores a vertical winning move in the left column with a +1"
      (let [current-board (b/mark-many :player-two [0 3])]
        (should= 1 (minimax :player-two :player-one current-board 6))))

  (it "scores a vertical winning move in the middle column with a +1"
      (let [current-board (b/mark-many :player-two [4 1])]
        (should= 1 (minimax :player-two :player-one current-board 7))))

  (it "scores a vertical winning move in the right column with a +1"
      (let [current-board (b/mark-many :player-one [5 8])]
        (should= 1 (minimax :player-one :player-two current-board 2))))

  (it "scores a left diagonal move with a +1"
      (let [current-board (b/mark-many :player-one [0 4])]
        (should= 1 (minimax :player-one :player-two current-board 8))))

  (it "scores a right diagonal move with a +1"
      (let [current-board (b/mark-many :player-two [2 6])]
        (should= 1 (minimax :player-two :player-one current-board 4))))

  (it "scores a tie move with a 0"
      (let [current-board almost-tie-game-board]
        (should= 0 (minimax :player-one :player-two current-board 1))))

  (it "scores a tie move with a 0 (complementary board)"
      (let [current-board complementary-almost-tie-game-board]
        (should= 0 (minimax :player-two :player-one current-board 7))))

  (it "scores a winning move on a full board with a +1"
      (let [current-board almost-tie-game-board]
        (should= 1 (minimax :player-two :player-one current-board 1))))

  (it "scores a winning move on a full board with a +1 (complementary board)"
      (let [current-board complementary-almost-tie-game-board]
        (should= 1 (minimax :player-one :player-two current-board 7))))

  (it "scores a move as -1 if the opponent can win horizontally on the next turn"
      (let [current-board (b/mark-many :player-one [1 2])]
        (should= -1 (minimax :player-two :player-one current-board 7))))

  (it "scores a move as -1 if the opponent can win vertically on the next turn"
      (let [current-board (b/mark-many :player-two [2 5])]
        (should= -1 (minimax :player-one :player-two current-board 1))))

  (it "scores a move as -1 if the opponent can win diagonally on the next turn"
      (let [current-board (b/mark-many :player-two [0 4])]
        (should= -1 (minimax :player-one :player-two current-board 1)))))

(describe "select best move"

  (it "selects a winning move from a list"
      (let [input [{0 -1} {1 0} {2 -1} {3 -1} {4 -1} {5 1} {6 -1} {7 -1} {8 0}]]
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

  (it "takes a tie move if it is the only move available"
      (let [current-board almost-tie-game-board
            expected-output (b/mark :player-one 1 almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a winning move if it is the only move available"
      (let [current-board almost-tie-game-board
            expected-output (b/mark :player-two 1 almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a tie move if it is the only move available (complementary board)"
      (let [current-board complementary-almost-tie-game-board
            expected-output (b/mark :player-two 7 complementary-almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "takes a winning move if it is the only move available (complementary board)"
      (let [current-board complementary-almost-tie-game-board
            expected-output (b/mark :player-one 7 complementary-almost-tie-game-board)]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a winning move out of two choices (player one)"
      (let [current-board (b/mark-many :player-two [1 4 2 8] (b/mark-many :player-one [0 3 5]))
            expected-output (b/mark :player-one 6 current-board)]
          (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "takes a winning move out of two choices (player two)"
      (let [current-board (b/mark-many :player-two [1 4 5 8] (b/mark-many :player-one [0 2 3]))
            expected-output (b/mark :player-two 7 current-board)]
          (should= expected-output (unbeatable-ai :player-two current-board))))

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

  (it "it is a fatalistic computer player"
      (let [current-board (b/mark-many :player-one [0 1 3])
            expected-output (b/mark :player-two 8 (b/mark-many :player-one [0 1 3]))]
        (should= expected-output (unbeatable-ai :player-two current-board))))

  (it "blocks an opponent who is about to win diagonally"
      (let [current-board (b/mark-many :player-one [0 7] (b/mark-many :player-two [4 6]))
            expected-output (b/mark-many :player-one [0 7 2] (b/mark-many :player-two [4 6]))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "picks a winning spot if two spots are available"
      (let [current-board (b/mark-many :player-one [0 1 3])
            expected-output (b/mark-many :player-one [0 1 3 6])]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "picks a winning spot over blocking a win (player one)"
      (let [current-board (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4]))
            expected-output (b/mark :player-one 5 (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4])))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "picks a winning spot over blocking a win (player two)"
      (let [current-board (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4]))
            expected-output (b/mark :player-two 8 (b/mark-many :player-two [6 7] (b/mark-many :player-one [3 4])))]
        (should= expected-output (unbeatable-ai :player-two current-board)))))
