(ns tic-tac-toe.game
  (require [tic-tac-toe.board :as board])
  (require [tic-tac-toe.rules :as rules])
  (require [tic-tac-toe.messages :as messages])
  (require [tic-tac-toe.computer-ai :as ai]))

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
    (not (= :empty (board/cell-status cell board))) :cell-taken
    :else :valid-move))

(defn- not-an-integer-action [board]
  (println messages/non-integer-input)
  board)

(defn- integer-too-small-action [board]
  (println messages/less-than-zero-input)
  board)

(defn- integer-too-large-action [board]
  (println messages/greater-than-eight-input)
  board)

(defn- cell-taken-action [board]
  (println messages/spot-taken-input)
  board)

(defn move
  ([cell] (move cell board/empty-board))
  ([cell board]
   (condp = (validate-move cell board)
    :not-an-integer (not-an-integer-action board)
    :integer-too-small (integer-too-small-action board)
    :integer-too-large (integer-too-large-action board)
    :cell-taken (cell-taken-action board)
    :valid-move (board/mark (current-player board) cell board))))

(defn- print-board [board]
  (println (str "\n" (messages/current-player (current-player board)) "\n"))
  (println (board/current-board board)))

(defn human [board]
  (move (string-to-number (read-line)) board))

(defn easy-computer [board]
  (ai/first-available-spot-ai (current-player board) board))

(defn hard-computer [board]
  (ai/unbeatable-ai (current-player board) board))

(defn execute-play-loop [play-fn player-one player-two board]
  (case (current-player board)
    :player-one (play-fn player-one player-two (player-one board))
    :player-two (play-fn player-one player-two (player-two board))))

(defn play [player-one player-two board]
   (if (rules/game-in-progress? board)
     (do
       (print-board board)
       (execute-play-loop play player-one player-two board))
     (do
       (println (str "\n" messages/game-over "\n" (messages/game-status (rules/game-status board)) "\n"))
       (println (board/current-board board)))))
