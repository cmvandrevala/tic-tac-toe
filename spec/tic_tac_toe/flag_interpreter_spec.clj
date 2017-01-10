(ns tic-tac-toe.flag-interpreter
  (:require [speclj.core :refer :all]
            [tic-tac-toe.flag-interpreter :refer :all]
            [tic-tac-toe.game :as g]))

(describe "console flags"

  (it "returns the human function for a human flag"
      (should= g/human (flag-to-function "human")))

  (it "returns the easy-computer function for the easy flag"
      (should= g/easy-computer (flag-to-function "easy")))

  (it "returns the hard-computer function for the hard flag"
      (should= g/hard-computer (flag-to-function "hard")))

  (it "returns the easy-computer function for the easy-computer flag"
      (should= g/easy-computer (flag-to-function "easy-computer")))

  (it "returns the hard-computer function for the hard-computer flag"
      (should= g/hard-computer (flag-to-function "hard-computer")))

  (it "returns the easy-computer for invalid input"
      (should= g/easy-computer (flag-to-function "invalid"))))
