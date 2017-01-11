(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r]))

(defn first-available-spot-ai [player board]
  (let [spaces (b/remaining-spaces board)]
    (b/mark player (apply min spaces) board)))

(defn- lazy-contains? [col key]
  (= key (some #{key} col)))

(defn- opponent [player]
  (if (= player :player-one) :player-two :player-one))

(defn- game-status-for-each-move [player board]
  (loop [spaces (b/remaining-spaces board) statuses []]
    (if (empty? spaces)
      statuses
      (recur (rest spaces) (conj statuses (r/game-status (b/mark player (first spaces) board)))))))

(defn- player-can-win-this-turn [player board]
  (let [statuses (game-status-for-each-move player board)]
    (lazy-contains? statuses player)))

(defn- eliminate-negative-utilities [moves-and-utilities]
  (if (empty? (filter #(not (= (first (vals %1)) -1)) moves-and-utilities))
    moves-and-utilities
    (filter #(not (= (first (vals %1)) -1)) moves-and-utilities)))

(defn- sorted-moves-and-utilities [moves-and-utilities]
  (let [moves-without-negative-utility (eliminate-negative-utilities moves-and-utilities)]
    (sort-by #(first (vals %1)) moves-without-negative-utility)))

(defn utility [player board cell]
  (let [status (r/game-status (b/mark player cell board))]
    (cond
      (= status player) 1
      (= status :tie) 0
      (and (= status :in-progress) (player-can-win-this-turn (opponent player) (b/mark player cell board))) -1
      :else nil)))

(defn utilities-for-remaining-cells [player board]
  (map #(hash-map %1 (utility player board %1)) (b/remaining-spaces board)))

(defn best-move [moves-and-utilities]
  (let [extract-move (comp first keys last sorted-moves-and-utilities)]
    (extract-move moves-and-utilities)))

(defn unbeatable-ai [player board]
  (let [moves-and-utilities (utilities-for-remaining-cells player board)]
    (b/mark player (best-move moves-and-utilities) board)))
