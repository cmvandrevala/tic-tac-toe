(ns tic-tac-toe.messages)

(def start-game "Welcome to Tic-Tac-Toe!")

(def player-one "It is player one's move.")

(def player-two "It is player two's move.")

(def game-over "The game is over.")

(def non-integer-input "You must enter an integer between 0 and 8!")

(def less-than-zero-input "Your move must be >= 0")

(def greater-than-eight-input "Your move must be <= 8")

(def spot-taken-input "That spot has already been taken!")

(defn current-player [player]
  (if (= player :player-one) player-one player-two))

(defn game-status [game-status]
  (case game-status
    :player-one "Player one wins!"
    :player-two "Player two wins!"
    "It's a tie!"))
