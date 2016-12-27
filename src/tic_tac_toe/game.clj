(ns tic-tac-toe.game
  (require [tic-tac-toe.board :as b]))

(defn current-player
  ([] :player-one)
  ([board] (if (even? (count board)) :player-one :player-two)))

(defn move
  ([cell] (b/mark (current-player) cell b/empty-board))
  ([cell board] (b/mark (current-player board) cell board)))
