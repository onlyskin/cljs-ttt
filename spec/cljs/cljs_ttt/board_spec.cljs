(ns cljs-ttt.board-spec 
  (:require-macros [speclj.core :refer [describe it should=]])
  (:require [speclj.core]
            [cljs-ttt.board :refer [make-board,
                                    play-on-board,
                                    available-moves,
                                    winner?
                                    winner
                                    tie?
                                    game-over?
                                    current-marker]]))

(defn- vec-for-string [board-string]
  (clojure.string/split board-string ""))

(describe "make-board"
  (it "returns nine string vector"
    (should=
      (vec-for-string "         ")
      (make-board))))

(describe "play-on-board"
  (it "plays on board"
    (should=
      (vec-for-string "   X O   ")
      (play-on-board (vec-for-string "   X     ") 6))))

(describe "available-moves"
  (it "gets available moves"
    (should=
      [2, 3, 8, 9]
      (available-moves (vec-for-string "X  OOXX  ")))))

(describe "winner?"
  (it "true for X win in row"
    (should= true
             (winner? (vec-for-string "XXXOO    ") "X")))

  (it "false for X win"
    (should= false
             (winner? (vec-for-string "XX OOOX  ") "X")))

  (it "true for O win in column"
    (should= true
             (winner? (vec-for-string "XOX O XO ") "O")))

  (it "true for X win on diagonal"
    (should= true
             (winner? (vec-for-string "XO  XO  X") "X"))))

(describe "winner"
  (it "O when O is winner"
    (should= "O"
             (winner (vec-for-string "XX OOOX  "))))

  (it "X when X is winner"
    (should= "X"
             (winner (vec-for-string "XXXOO    ")))))

(describe "tie?"
  (it "false when full board with winner"
    (should= false
             (tie? (vec-for-string "XOOOOXXXX"))))

  (it "true when full board no winner"
    (should= true
             (tie? (vec-for-string "XOXOOXXXO"))))

  (it "false when not full with winner"
    (should= false
             (tie? (vec-for-string "  X XOXO "))))

  (it "false when not full no winner"
    (should= false
             (tie? (vec-for-string "    XOXO ")))))

(describe "game-over?"
  (it "true when full board with winner"
    (should= true
             (game-over? (vec-for-string "XOOOOXXXX"))))

  (it "true when full board no winner"
    (should= true
             (game-over? (vec-for-string "XOXOOXXXO"))))

  (it "true when not full with winner"
    (should= true
             (game-over? (vec-for-string "  X XOXO "))))

  (it "false when not full no winner"
    (should= false
             (game-over? (vec-for-string "    XOXO ")))))

(describe "current-marker"
  (it "X on first turn"
    (should=
      "X"
      (current-marker (vec-for-string "         "))))

  (it "X on first turn"
    (should=
      "O"
      (current-marker (vec-for-string "XX OO X  ")))))
