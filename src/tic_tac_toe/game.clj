(ns tic-tac-toe.game
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r])
  (require [tic-tac-toe.messages :as m]))

(defn string-to-number [str]
  (let [n (read-string str)]
    (if (number? n) (int n) 10)))

(defn current-player
  ([] :player-one)
  ([board] (if (even? (count board)) :player-one :player-two)))

(defn move
  ([cell] (b/mark (current-player) cell b/empty-board))
  ([cell board] (b/mark (current-player board) cell board)))

(defn play
  ([]
   (println (str "\n" m/start-game "\n" m/player-one "\n"))
   (println (b/current-board))
   (play (move (string-to-number (read-line)))))
  ([board]
   (if (r/game-in-progress? board)
     (do
      (println (str "\n" (m/current-player (current-player board)) "\n"))
      (println (b/current-board board))
      (play (move (string-to-number (read-line)) board)))
     (do
       (println (str "\n" m/game-over "\n" (m/game-status (r/game-status board)) "\n"))
       (println (b/current-board board))))))
