(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r])
  (require [tic-tac-toe.game-tree :as gt]))

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

(defn utility [player board]
  (let [status (r/game-status board)]
    (condp = status
        player 1
        :tie 0
        (opponent player) -1
        :in-progress nil)))

(defn- opponent-can-win-next-turn [player board]
  (let [statuses (game-status-for-each-move player board)]
    (lazy-contains? statuses player)))

(defn- sort-moves-and-utilities [moves-and-utilities]
  (sort-by #(first (vals %1)) moves-and-utilities))

(defn minimax [player opponent board cell]
  (let [marked-board (b/mark player cell board)
        status (r/game-status marked-board)
        move-tree (map #(minimax opponent player marked-board %1) (b/remaining-spaces marked-board))]
    (cond
      (= status player) 1
      (= status :tie) 0
      (opponent-can-win-next-turn opponent marked-board) -1
      :else (first move-tree))))

(defn- utilities-for-remaining-cells [player board]
  (map #(hash-map %1 (minimax player (opponent player) board %1)) (b/remaining-spaces board)))

(defn best-move [moves-and-utilities]
  (let [extract-move (comp first keys last sort-moves-and-utilities)]
    (extract-move moves-and-utilities)))

(defn unbeatable-ai [player board]
  (let [moves-and-utilities (utilities-for-remaining-cells player board)]
    (b/mark player (best-move moves-and-utilities) board)))
