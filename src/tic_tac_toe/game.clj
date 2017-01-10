(ns tic-tac-toe.game
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r])
  (require [tic-tac-toe.messages :as m])
  (require [tic-tac-toe.computer-ai :as c]))

(defn string-to-number [str]
  (let [n (read-string str)]
    (if (number? n) (int n) 10)))

(defn current-player
  ([] :player-one)
  ([board] (if (even? (count board)) :player-one :player-two)))

(defn opponent [player]
  (if (= player :player-one)
    :player-two
    :player-one))

(defn move
  ([cell] (b/mark (current-player) cell b/empty-board))
  ([cell board] (b/mark (current-player board) cell board)))

(defn- print-board [board]
  (println (str "\n" (m/current-player (current-player board)) "\n"))
  (println (b/current-board board)))

(defn human [board]
  (move (string-to-number (read-line)) board))

(defn dumb-computer [board]
  (c/first-available-spot-ai (current-player board) board))

(defn execute-play-loop [play-fn player-one player-two board]
  (case (current-player board)
    :player-one (play-fn player-one player-two (player-one board))
    :player-two (play-fn player-one player-two (player-two board))))

(defn play [player-one player-two board]
   (if (r/game-in-progress? board)
     (do
       (print-board board)
       (execute-play-loop play player-one player-two board))
     (do
       (println (str "\n" m/game-over "\n" (m/game-status (r/game-status board)) "\n"))
       (println (b/current-board board)))))
