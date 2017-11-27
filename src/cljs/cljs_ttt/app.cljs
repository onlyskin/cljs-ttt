(ns cljs-ttt.app
  (:require [cljs-ttt.draw :refer [draw-page]]
            [cljs-ttt.board :refer [available-moves make-board]]))

(def game-state (atom {:board (make-board)}))

(defn run []
  (draw-page game-state))

(defn- moves-count []
  (->> (@game-state :board)
       (available-moves)
       (count)))

(defn empty-game? []
  (= 9 (moves-count)))

(defn finished-game? []
  (= 0 (moves-count)))

(defn in-progress-game? []
  (and
      (not= 9 (moves-count))
      (not= 0 (moves-count))))
