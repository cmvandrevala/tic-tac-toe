(ns tic-tac-toe.game
  (require [tic-tac-toe.board :as b])
  (require [tic-tac-toe.rules :as r])
  (require [tic-tac-toe.messages :as m])
  (require [tic-tac-toe.computer-ai :as c]))

(defn string-to-number [str]
  (when (not (or (nil? str) (= "" str)))
    (let [n (read-string str)]
      (when (number? n) n))))

(defn current-player
  ([] :player-one)
  ([board] (if (even? (count board)) :player-one :player-two)))

(defn validate-move [cell board]
  (cond
    (nil? cell) :not-an-integer
    (not (integer? cell)) :not-an-integer
    (< cell 0) :integer-too-small
    (> cell 8) :integer-too-large
    (not (= :empty (b/cell-status cell board))) :cell-taken
    :else :valid-move))

(defn move
  ([cell] (move cell b/empty-board))
  ([cell board]
   (condp = (validate-move cell board)
    :not-an-integer
    (do
      (println "You must enter an integer between 0 and 8!")
      board)
    :integer-too-small
    (do
      (println "Your move must be >= 0")
      board)
    :integer-too-large
    (do
      (println "Your move must be <= 8")
      board)
    :cell-taken
    (do
      (println "That spot has already been taken!")
      board)
    :valid-move (b/mark (current-player board) cell board))))

(defn- print-board [board]
  (println (str "\n" (m/current-player (current-player board)) "\n"))
  (println (b/current-board board)))

(defn human [board]
  (move (string-to-number (read-line)) board))

(defn easy-computer [board]
  (c/first-available-spot-ai (current-player board) board))

(defn hard-computer [board]
  (c/unbeatable-ai (current-player board) board))

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
