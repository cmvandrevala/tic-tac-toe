(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r]))

(defn first-available-spot-ai [player board]
  (let [spaces (b/remaining-spaces board)]
    (b/mark player (apply min spaces) board)))

(defn utility [player cell board]
  (let [status (r/game-status (b/mark player cell board))]
    (cond
      (= status player) 1
      (= status :tie) 0
      :else nil)))

(defn unbeatable-ai [player board]
  (loop [available-spaces (b/remaining-spaces board)
         current-best-move (first available-spaces)]
    (if (empty? available-spaces)
      (do
        (b/mark player current-best-move board))
      (do
        (let [utility-of-first-move (utility player (first available-spaces) board)]
          (cond
            (= 1 utility-of-first-move) (b/mark player (first available-spaces) board)
            (= 0 utility-of-first-move) (recur (rest available-spaces) current-best-move)
            :else (recur (rest available-spaces) current-best-move)))))))
