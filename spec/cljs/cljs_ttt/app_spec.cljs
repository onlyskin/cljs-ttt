(ns cljs-ttt.app-spec 
  (:require-macros [speclj.core :refer [describe
                                        it
                                        should=
                                        should-have-invoked
                                        with-stubs
                                        stub]])
  (:require [speclj.core]
            [cljs-ttt.app :refer [run
                                  game-state
                                  empty-game?]]  
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
    (with-redefs [game-state (atom {:board (vec-for-string "         ")})]
      (should=
        true
        (empty-game?))))

  (it "is false if game-state board not empty"
    (with-redefs [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        false
        (empty-game?)))))
