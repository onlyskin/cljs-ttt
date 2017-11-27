(ns cljs-ttt.app-spec 
  (:require-macros [speclj.core :refer [describe
                                        it
                                        should=
                                        should-have-invoked
                                        with-stubs
                                        stub]])
  (:require [speclj.core]
            [cljs-ttt.app :refer [run
                                  restart
                                  game-state
                                  empty-game?
                                  finished-game?
                                  in-progress-game?]]  
            [cljs-ttt.draw :refer [draw-page]]))

(defn- vec-for-string [board-string]
  (clojure.string/split board-string ""))

(describe "run"
  (with-stubs)

  (it "calls draw-page with state"
    (with-redefs [draw-page (stub :draw-page)]
      (run)

      (should-have-invoked :draw-page {:with [game-state]})))

  (it "inits game-state with empty board"
    (run)

    (should=
      (vec-for-string "         ")
      (@game-state :board))))

(describe "empty-game?"
  (it "is true if game-state board is empty"
    (with-redefs
      [game-state (atom {:board (vec-for-string "         ")})]
      (should=
        true
        (empty-game?))))

  (it "is false if game-state board not empty"
    (with-redefs [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        false
        (empty-game?)))))

(describe "finished-game?"
  (it "is true if game-state board is full"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XXOOOXXOX")})]
      (should=
        true
        (finished-game?))))

  (it "is false if game-state board is not full"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        false
        (finished-game?)))))
 
(describe "in-progress-game?"
  (it "is false if game-state board is empty"
    (with-redefs
      [game-state (atom {:board (vec-for-string "         ")})]
      (should=
        false
        (in-progress-game?))))

  (it "is true if game-state board has some moves"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        true
        (in-progress-game?))))

  (it "is false if game-state board is full"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XXOOOXXOX")})]
      (should=
        false
        (in-progress-game?)))))

(describe "restart"
  (with-stubs)

  (it "calls draw-page with state"
    (with-redefs [draw-page (stub :draw-page)]
      (restart)

      (should-have-invoked :draw-page {:with [game-state]})))

  (it "sets game-state board back to an empty board"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XXO      ")})]
      (restart)

      (should=
        (vec-for-string "         ")
        (@game-state :board)))))
