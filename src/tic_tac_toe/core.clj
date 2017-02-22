(ns tic-tac-toe.core
  (:require [tic-tac-toe.game :as game])
  (:require [tic-tac-toe.board :as board])
  (:require [tic-tac-toe.flag-interpreter :as interpreter]))

(defn -main [& args]
  (game/play (interpreter/flag-to-function (first args))
          (interpreter/flag-to-function (last args))
          board/empty-board))
