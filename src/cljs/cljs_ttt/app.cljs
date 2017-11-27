(ns cljs-ttt.app
  (:require [cljs-ttt.draw :refer [draw-page]]
            [cljs-ttt.computer-player :refer [get-negamax-move]]
            [cljs-ttt.board :refer [available-moves
                                    make-board
                                    play-on-board]]))

(def player-for-index {0 :p0 1 :p1})
(defn- player-count [] (count player-for-index))

(def game-state (atom {:board (make-board)
                       (player-for-index 0) :human
                       (player-for-index 1) :human}))

(defn run []
  (draw-page game-state))

(defn restart []
  (swap! game-state assoc :board (make-board))
  (draw-page game-state))

(defn set-player [index value]
  (swap! game-state assoc (player-for-index index) value)
  (draw-page game-state))

(defn- moves-count []
  (->> (@game-state :board)
       (available-moves)
       (count)))

(defn- current-p [] 
  (nth (vals player-for-index) (mod (- 9 (moves-count)) (player-count))))

(defn- computer-next? []
  (= :computer (@game-state (current-p))))

(defn empty-game? []
  (= 9 (moves-count)))

(defn finished-game? []
  (= 0 (moves-count)))

(defn in-progress-game? []
  (and
      (not= 9 (moves-count))
      (not= 0 (moves-count))))

(defn- play-on-game-state [position]
  (swap!
    game-state
    assoc :board (play-on-board (@game-state :board) position)))

(defn play [position]
  (play-on-game-state position)
  (cond
    (and (not (finished-game?)) (computer-next?))
    (play-on-game-state (get-negamax-move (@game-state :board))))
  (draw-page game-state))
