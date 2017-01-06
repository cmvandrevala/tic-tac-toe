(ns tic-tac-toe.flag-interpreter
  (:require [speclj.core :refer :all]
            [tic-tac-toe.flag-interpreter :refer :all]
            [tic-tac-toe.game :as g]))

(describe "console flags"

  (it "returns the human function for a human flag"
      (should= g/human (flag-to-function "human")))

  (it "returns the dumb-computer function for a computer flag"
      (should= g/dumb-computer (flag-to-function "computer"))))
