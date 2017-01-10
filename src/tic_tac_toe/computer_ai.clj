(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r]))

(defn first-available-spot-ai [player board]
  (let [spaces (b/remaining-spaces board)]
    (b/mark player (apply min spaces) board)))

(defn utility [player board cell]
  (let [status (r/game-status (b/mark player cell board))]
    (cond
      (= status player) 1
      (= status :tie) 0
      :else nil)))

(defn utilities-for-remaining-cells [player board]
  (map #(hash-map %1 (utility player board %1)) (b/remaining-spaces board)))

(defn unbeatable-ai [player board]
  (let [utilities (utilities-for-remaining-cells player board)
        default-spot (first (b/remaining-spaces board))
        cell (or (first (keys (first (filter #(not (nil? (first (vals %1)))) utilities)))) default-spot)]
      (b/mark player cell board)))
