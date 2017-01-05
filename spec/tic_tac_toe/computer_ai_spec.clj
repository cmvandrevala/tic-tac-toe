(ns tic-tac-toe.computer-ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-ai :refer :all]
            [tic-tac-toe.board :as b]))

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
