(ns tic-tac-toe.game
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r]))

(defn- string-to-number [str]
  (let [n (read-string str)]
    (if (number? n) n nil)))

(defn current-player
  ([] :player-one)
  ([board] (if (even? (count board)) :player-one :player-two)))

(defn move
  ([cell] (b/mark (current-player) cell b/empty-board))
  ([cell board] (b/mark (current-player board) cell board)))

(defn play
  ([] (play (move (string-to-number (read-line)))))
  ([board] (println board) (play (move (string-to-number (read-line)) board))))
