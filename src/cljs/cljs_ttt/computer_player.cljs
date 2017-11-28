(ns cljs-ttt.computer-player
  (:require [cljs-ttt.board :refer [tie?
                                    winner?
                                    game-over?
                                    current-marker
                                    available-moves
                                    play-on-board]]))

(defn- winning-score [] 1000)
(defn- losing-score [] -1000)

(defn- score [board]
  (cond
    (tie? board) 0
    (winner? board (current-marker board)) (winning-score)
    :else (losing-score)))

(defn- max-by-score [scores]
  (apply max-key val scores)) 

(defn- best-move [scores] (key (max-by-score scores)))
(defn- best-score [scores] (val (max-by-score scores)))

(declare negamax)
(defn score-moves [depth board moves]
  (map #(- (negamax (inc depth) (play-on-board board %))) moves))

(defn negamax [depth board]
  (cond

    (game-over? board)
    (score board)

    :else
    (let [moves (available-moves board)
          scores (score-moves depth board moves)
          nodes (zipmap moves scores)]
      (cond
        (= 0 depth) (best-move nodes)
        :else (best-score nodes)))))

(defn get-negamax-move [board]
  (negamax 0 board))
