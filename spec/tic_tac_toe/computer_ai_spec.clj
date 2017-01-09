(ns tic-tac-toe.computer-ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-ai :refer :all]
            [tic-tac-toe.board :as b]))

(def almost-tie-game-board
    (b/mark :player-two 8
      (b/mark :player-two 6
        (b/mark-many :player-two [1 3 4]
          (b/mark-many :player-one [0 2 5])))))

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

(describe "an unbeatable computer player"

  (it "takes a horizontal winning move when it is available"
      (let [current-board (b/mark-many :player-one [0 1])
            expected-output (b/mark :player-one 2 (b/mark :player-one 1 (b/mark :player-one 0 b/empty-board)))]
        (should= expected-output (unbeatable-ai :player-one current-board))))

  (it "scores a horizontal winning move with a +1"
      (let [current-board (b/mark-many :player-one [0 1])]
        (should= 1 (score-move :player-one 2 current-board))))

  (it "scores a vertical winning move with a +1"
      (let [current-board (b/mark-many :player-two [4 1])]
        (should= 1 (score-move :player-two 7 current-board))))

  (it "scores a horizontal losing move with a -1"
      (let [current-board (b/mark-many :player-one [4 5])]
        (should= -1 (score-move :player-two 3 current-board))))

  (it "scores a diagonal losing move with a -1"
      (let [current-board (b/mark-many :player-one [4 2])]
        (should= -1 (score-move :player-two 6 current-board))))

  (it "scores a tie move with a 0"
      (let [current-board almost-tie-game-board]
        (should= 0 (score-move :player-one 7 current-board)))))
