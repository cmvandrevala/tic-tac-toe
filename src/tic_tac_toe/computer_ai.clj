(ns tic-tac-toe.computer-ai
  (require [tic-tac-toe.board :as board])
  (require [tic-tac-toe.rules :as rules])
  (require [tic-tac-toe.game-tree :as game-tree]))

(defn first-available-spot-ai [player board]
  (let [spaces (board/remaining-spaces board)]
    (board/mark player (apply min spaces) board)))

(defn- opponent [player]
  (if (= player :player-one) :player-two :player-one))

(defn utility [player board]
  (let [status (rules/game-status board)]
    (condp = status
        player 1
        :tie 0
        (opponent player) -1
        :in-progress nil)))

(defn generate-game-tree
  ([player board] (generate-game-tree player board game-tree/nil-node 0))
  ([player board node level]
   (loop [remaining-spaces (board/remaining-spaces board) current-tree node]
     (if (empty? remaining-spaces)
       current-tree
       (let [current-space (first remaining-spaces)
             marked-board (board/mark player current-space board)
             value (utility player marked-board)]
           (if (rules/game-in-progress? marked-board)
             (recur (rest remaining-spaces) (game-tree/add-child {:player player :cell current-space :value value :children (:children (generate-game-tree (opponent player) marked-board game-tree/nil-node (inc level)))} current-tree))
             (recur (rest remaining-spaces) (game-tree/add-child {:player player :cell current-space :value (if (even? level) value (* -1 value))} current-tree))))))))

(defn- sort-moves-and-utilities [moves-and-utilities]
  (sort-by #(first (vals %1)) moves-and-utilities))

(defn minimax [player cell board]
  (let [marked-board (board/mark player cell board)]
    (if (rules/game-in-progress? marked-board)
      (* -1 (game-tree/score (generate-game-tree (opponent player) marked-board)))
      (utility player marked-board))))

(defn- utilities-for-remaining-cells [player board]
  (map #(hash-map %1 (minimax player %1 board)) (board/remaining-spaces board)))

(defn best-move [moves-and-utilities]
  (let [extract-move (comp first keys last sort-moves-and-utilities)]
    (extract-move moves-and-utilities)))

(defn unbeatable-ai [player board]
  (cond
    (board/empty-board? board) (board/mark player 0 board)
    (and (= 1 (count board)) (= :empty (board/cell-status 4 board))) (board/mark player 4 board)
    (and (= 1 (count board)) (not (= :empty (board/cell-status 4 board)))) (board/mark player 0 board)
    :else (let [moves-and-utilities (utilities-for-remaining-cells player board)]
            (board/mark player (best-move moves-and-utilities) board))))
