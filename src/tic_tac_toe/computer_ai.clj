(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r])
  (require [tic-tac-toe.game-tree :as gt]))

(defn first-available-spot-ai [player board]
  (let [spaces (b/remaining-spaces board)]
    (b/mark player (apply min spaces) board)))

(defn- opponent [player]
  (if (= player :player-one) :player-two :player-one))

(defn utility [player board]
  (let [status (r/game-status board)]
    (condp = status
        player 1
        :tie 0
        (opponent player) -1
        :in-progress nil)))

(defn generate-game-tree
  ([player board] (generate-game-tree player board gt/nil-node 0))
  ([player board node level]
   (loop [remaining-spaces (b/remaining-spaces board) current-tree node]
     (if (empty? remaining-spaces)
       current-tree
       (let [current-space (first remaining-spaces)
             marked-board (b/mark player current-space board)
             value (utility player marked-board)]
           (if (r/game-in-progress? marked-board)
             (recur (rest remaining-spaces) (gt/add-child {:player player :cell current-space :value value :children (:children (generate-game-tree (opponent player) marked-board gt/nil-node (inc level)))} current-tree))
             (recur (rest remaining-spaces) (gt/add-child {:player player :cell current-space :value (if (even? level) value (* -1 value))} current-tree))))))))

(defn- sort-moves-and-utilities [moves-and-utilities]
  (sort-by #(first (vals %1)) moves-and-utilities))

(defn minimax [player cell board]
  (let [marked-board (b/mark player cell board)]
    (if (r/game-in-progress? marked-board)
      (* -1 (gt/score (generate-game-tree (opponent player) marked-board)))
      (utility player marked-board))))

(defn- utilities-for-remaining-cells [player board]
  (map #(hash-map %1 (minimax player %1 board)) (b/remaining-spaces board)))

(defn best-move [moves-and-utilities]
  (let [extract-move (comp first keys last sort-moves-and-utilities)]
    (extract-move moves-and-utilities)))

(defn unbeatable-ai [player board]
  (cond
    (b/empty-board? board) (b/mark player 0 board)
    (and (= 1 (count board)) (= :empty (b/cell-status 4 board))) (b/mark player 4 board)
    (and (= 1 (count board)) (not (= :empty (b/cell-status 4 board)))) (b/mark player 0 board)
    :else (let [moves-and-utilities (utilities-for-remaining-cells player board)]
            (b/mark player (best-move moves-and-utilities) board))))
