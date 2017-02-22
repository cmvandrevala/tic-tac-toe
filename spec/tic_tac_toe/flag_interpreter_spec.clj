(ns tic-tac-toe.flag-interpreter
  (:require [speclj.core :refer :all]
            [tic-tac-toe.flag-interpreter :refer :all]
            [tic-tac-toe.game :as game]))

(describe "console flags"

  (it "returns the human function for a human flag"
      (should= game/human (flag-to-function "human")))

  (it "returns the easy-computer function for the easy flag"
      (should= game/easy-computer (flag-to-function "easy")))

  (it "returns the hard-computer function for the hard flag"
      (should= game/hard-computer (flag-to-function "hard")))

  (it "returns the easy-computer function for the easy-computer flag"
      (should= game/easy-computer (flag-to-function "easy-computer")))

  (it "returns the hard-computer function for the hard-computer flag"
      (should= game/hard-computer (flag-to-function "hard-computer")))

  (it "returns the easy-computer for invalid input"
      (should= game/easy-computer (flag-to-function "invalid"))))
