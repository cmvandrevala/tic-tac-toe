(ns tic-tac-toe.core
  (:require [tic-tac-toe.game :as g])
  (:require [tic-tac-toe.board :as b]))

(defn -main [] (g/play g/human g/dumb-computer b/empty-board))
