(ns cljs-ttt.app
  (:require [cljs-ttt.draw :refer [draw-page]]
            [cljs-ttt.board :refer [available-moves make-board]]))

(def game-state (atom {:board (make-board)}))

(defn run []
  (draw-page game-state))

(defn empty-game? []
  (->> (@game-state :board)
       (available-moves)
       (count)
       (= 9)))
