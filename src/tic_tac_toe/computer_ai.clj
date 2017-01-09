(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r]))

(defn first-available-spot-ai [player board]
  (let [spaces (b/remaining-spaces board)]
    (b/mark player (apply min spaces) board)))

(defn score-move [player cell board]
  (if (= player (r/game-status (b/mark player cell board)))
    1
    (if (= :tie (r/game-status (b/mark player cell board)))
      0
      -1)))

(defn unbeatable-ai [player board]
  (b/mark player 2 board))
