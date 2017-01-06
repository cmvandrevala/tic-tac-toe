(ns tic-tac-toe.core
  (:require [tic-tac-toe.game :as g])
  (:require [tic-tac-toe.board :as b])
  (:require [tic-tac-toe.flag-interpreter :as f]))

(defn -main [& args]
  (g/play (f/flag-to-function (first args))
          (f/flag-to-function (last args))
          b/empty-board))
