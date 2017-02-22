(ns tic-tac-toe.flag-interpreter
  (require [tic-tac-toe.game :as game]))

(defn flag-to-function [flag]
  (case flag
    "human" game/human
    "easy" game/easy-computer
    "hard" game/hard-computer
    "easy-computer" game/easy-computer
    "hard-computer" game/hard-computer
    game/easy-computer))
