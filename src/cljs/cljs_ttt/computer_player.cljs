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

(declare negamax)
(defn f [state move]
  (cond
    (state :break) state
    :else (let
            [new-board (play-on-board (state :board) move)
             negamax-result (negamax
                         {:board new-board
                          :best-move nil
                          :best-score -10000
                          :alpha (- (state :beta))
                          :beta (- (state :alpha))
                          :colour (- (state :colour))
                          :break false})
             best-node [(first negamax-result)
                        (- (last negamax-result))]
             best-score (max (last best-node) (state :best-score))
             best-move (if (> (last best-node) (state :best-score)) move (state :best-move))
             alpha (max (state :alpha) (last best-node))
             break (>= (state :alpha) (state :beta))]
            {:board (state :board)
             :best-score best-score
             :best-move best-move
             :alpha alpha
             :beta (state :beta)
             :colour (state :colour)
             :break break})))

(defn negamax [state]
  (cond

    (game-over? (state :board))
    [nil (* (state :colour) (score (state :board)))]

    :else
    (let [moves (available-moves (state :board))
          result (reduce f state moves)]
      [(result :best-move) (result :best-score)])))

(defn get-negamax-move [board]
  (let [initial-state {:board board
                       :best-score -10000
                       :best-move nil
                       :alpha -10000
                       :beta 10000
                       :colour 1
                       :break false}]
    (first (negamax initial-state))))
