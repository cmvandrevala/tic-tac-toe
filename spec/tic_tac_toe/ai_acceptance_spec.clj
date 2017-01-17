(ns tic-tac-toe.computer-ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-ai :as ai]
            [tic-tac-toe.board :as b]
            [tic-tac-toe.rules :as r]
            [clojure.set]))

(defn play-all-human-moves-for-turn [single-board]
  (map #(b/mark :player-one % single-board) (b/remaining-spaces single-board)))

(defn conditionally-play-ai-move-on-boards [list-of-boards]
  (map #(if (r/game-in-progress? %) (ai/unbeatable-ai :player-two %) %) list-of-boards))

(defn player-then-computer [board]
  (conditionally-play-ai-move-on-boards (play-all-human-moves-for-turn board)))

(defn expand-moves [list-of-boards]
  (apply concat (map player-then-computer list-of-boards)))

(defn play-all [board]
  (let [level-one (player-then-computer board)
        level-two (expand-moves level-one)
        level-three (expand-moves level-two)
        level-four (expand-moves (filter r/game-in-progress? level-three))
        level-five (expand-moves (filter r/game-in-progress? level-four))
        statuses (set (map r/game-status level-five))]
      (if (contains? statuses :player-one) :fail :pass)))

(describe "play every game to ensure that the computer player is unbeatable"

  (it "wins or ties every possible game"
    (should= :pass (play-all b/empty-board))))
