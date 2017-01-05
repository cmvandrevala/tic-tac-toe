(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b]))

(defn first-available-spot-ai [player board]
  (let [spaces (b/remaining-spaces board)]
    (b/mark player (apply min spaces) board)))
