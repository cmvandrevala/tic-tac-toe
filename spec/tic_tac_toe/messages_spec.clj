(ns tic-tac-toe.messages-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.messages :refer :all]))

(describe "game messages on the console"

  (it "starts the game with a message"
      (should= "Welcome to Tic-Tac-Toe!" start-game))

  (it "has a message for player one's turn"
      (should= "It is player one's move." player-one))

  (it "has a message for player two's turn"
      (should= "It is player two's move." player-two))

  (it "has a message for the end of the game"
      (should= "The game is over." game-over))

  (it "returns a message for player one's turn, given the correct player"
      (should= "It is player one's move." (current-player :player-one)))

  (it "returns a message for player two's turn, given the correct player"
      (should= "It is player two's move." (current-player :player-two)))

  (it "returns a message when player one wins"
      (should= "Player one wins!" (game-status :player-one-wins)))

  (it "returns a message when player two wins"
      (should= "Player two wins!" (game-status :player-two-wins)))

  (it "returns a message when the game is a tie"
      (should= "It's a tie!" (game-status :tie))))
