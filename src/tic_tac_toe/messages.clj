(ns tic-tac-toe.messages)

(def start-game "Welcome to Tic-Tac-Toe!")

(def player-one "It is player one's move.")

(def player-two "It is player two's move.")

(def game-over "The game is over.")

(defn current-player [player]
  (if (= player :player-one) player-one player-two))

(defn game-status [game-status]
  (case game-status
    :player-one-wins "Player one wins!"
    :player-two-wins "Player two wins!"
    "It's a tie!"))
