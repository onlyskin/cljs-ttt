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

(declare negamax)
(defn make-nodes [board moves]
  (let [nodes (->> moves
                   (map #(->> %
                              (play-on-board board)
                              (negamax))))]
    (map #(vector %1 (- (last %2))) moves nodes)))


(defn negamax [board]
  (cond

    (game-over? board)
    [nil (score board)]

    :else
    (let [moves (available-moves board)
          nodes (make-nodes board moves)]
      (max-by-score nodes))))

(defn get-negamax-move [board]
  (first (negamax board)))
