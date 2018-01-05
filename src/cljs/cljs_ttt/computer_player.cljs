(ns cljs-ttt.computer-player
  (:require [cljs-ttt.board :refer [tie?
                                    winner?
                                    game-over?
                                    current-marker
                                    available-moves
                                    play-on-board]]))

(defn- winning-score [] 1000)
(defn- losing-score [] -1000)

(defn- score [state]
  (cond
    (tie? (state :board)) 0

    (winner? (state :board) (state :marker))
    (/ (state :depth) (winning-score))

    :else (/ (state :depth) (losing-score))))

(declare negamax)
(defn f [state move]
  (cond
    (state :break) state

    :else (let
            [new-board (play-on-board (state :board) move)
             negamax-result (negamax
                         {:board new-board
                          :depth (+ 1 (state :depth))
                          :best-move nil
                          :best-score (state :best-score)
                          :alpha (- (state :beta))
                          :beta (- (state :alpha))
                          :colour (- (state :colour))
                          :break false
                          :marker (state :marker)})
             best-node [(first negamax-result)
                        (- (last negamax-result))]
             best-score (max (last best-node) (state :best-score))
             best-move (if (> (last best-node) (state :best-score))
                         move
                         (state :best-move))
             alpha (max (state :alpha) (last best-node))
             break (>= (state :alpha) (state :beta))]

            {:board (state :board)
             :depth (state :depth)
             :best-score best-score
             :best-move best-move
             :alpha alpha
             :beta (state :beta)
             :colour (state :colour)
             :break break
             :marker (state :marker)})))

(defn negamax [state]
  (cond
    (game-over? (state :board))
      [nil (* (state :colour) (score state))]

    :else
    (let [moves (available-moves (state :board))
          result (reduce f state moves)]
      [(result :best-move) (result :best-score)])))

(defn get-negamax-move [board]
  (let [initial-state {:board board
                       :depth 0
                       :best-score -10000
                       :best-move nil
                       :alpha -10000
                       :beta 10000
                       :colour 1
                       :break false
                       :marker (current-marker board)}]
    (first (negamax initial-state))))
