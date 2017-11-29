(ns cljs-ttt.app
  (:require [cljs-ttt.draw :refer [draw-page]]
            [cljs-ttt.computer-player :refer [get-negamax-move]]
            [cljs-ttt.board :refer [available-moves
                                    make-board
                                    play-on-board]]))

(def root-element (.querySelector js/document "body"))

(declare config)

(def player-for-index {0 :p0 1 :p1})
(defn- player-count [] (count player-for-index))

(def game-state (atom {:board (make-board)
                       (player-for-index 0) :human
                       (player-for-index 1) :human}))

(defn restart []
  (swap! game-state assoc :board (make-board))
  (draw-page root-element game-state config))

(defn set-player [index value]
  (swap! game-state assoc (player-for-index index) value)
  (draw-page root-element game-state config))

(defn- moves-count []
  (->> (@game-state :board)
       (available-moves)
       (count)))

(defn- current-p [] 
  (nth (vals player-for-index) (mod (- 9 (moves-count)) (player-count))))

(defn- computer-next? []
  (= :computer (@game-state (current-p))))

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

(defn play [row-index cell-index]
  (play-on-game-state (+ 1 cell-index (* 3 row-index)))
  (cond
    (and (not (finished-game?)) (computer-next?))
    (play-on-game-state (get-negamax-move (@game-state :board))))
  (draw-page root-element game-state config))

(def config {:cell-click play})

(defn run []
  (draw-page root-element game-state config))

