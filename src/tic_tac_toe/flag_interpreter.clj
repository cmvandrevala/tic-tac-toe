(ns tic-tac-toe.flag-interpreter
  (require [tic-tac-toe.game :as g]))

(defn flag-to-function [flag]
  (if (= flag "human") g/human g/dumb-computer))
