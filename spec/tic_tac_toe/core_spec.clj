(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :refer :all]))

(describe "A generic passing test"
  (it "returns true for two equal numbers"
    (should= 0 0)))
