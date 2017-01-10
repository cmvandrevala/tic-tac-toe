(ns tic-tac-toe.flag-interpreter
  (require [tic-tac-toe.game :as g]))

(defn flag-to-function [flag]
  (case flag
    "human" g/human
    "easy" g/easy-computer
    "hard" g/hard-computer
    "easy-computer" g/easy-computer
    "hard-computer" g/hard-computer
    g/easy-computer))
