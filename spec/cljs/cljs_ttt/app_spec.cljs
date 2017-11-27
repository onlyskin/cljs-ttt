(ns cljs-ttt.app-spec 
  (:require-macros [speclj.core :refer [describe
                                        it
                                        should=
                                        should-have-invoked
                                        should-not-have-invoked
                                        with-stubs
                                        stub]])
  (:require [speclj.core]
            [cljs-ttt.app :refer [run
                                  restart
                                  play
                                  set-player
                                  game-state
                                  current-p
                                  empty-game?
                                  finished-game?
                                  in-progress-game?]]  
            [cljs-ttt.draw :refer [draw-page]]
            [cljs-ttt.computer-player :refer [get-negamax-move]]
            ))

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

(describe "play"
  (with-stubs)

  (it "calls draw-page with state"
    (with-redefs
      [draw-page (stub :draw-page)
       game-state (atom {:board (vec-for-string "         ")
                         :p0 :human
                         :p1 :human})]
      (play 3)

      (should-have-invoked :draw-page {:with [game-state]})))

  (it "plays on the board"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOX O    ")
                         :p0 :human
                         :p1 :human})]
      (play 8)

      (should=
        (vec-for-string "XOX O  X ")
        (@game-state :board))))

  (it "plays computer move if the next player is a computer"
    (with-redefs
      [game-state (atom {:board (vec-for-string "X   O    ")
                         :p0 :human
                         :p1 :computer})
       get-negamax-move (stub :get-negamax-move {:return 2})]

      (play 3)

      (should=
        (@game-state :board)
        (vec-for-string "XOX O    ")))) 

  (it "doesn't play if there are no more moves"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOXXOOOX ")
                         :p0 :human
                         :p1 :computer})
       get-negamax-move (stub :get-negamax-move {:return 2})]

      (play 9)

      (should-not-have-invoked :get-negamax-move)
      (should=
        (@game-state :board)
        (vec-for-string "XOXXOOOXX")))))

(describe "set player"
  (with-stubs)

  (it "sets p0 to :computer"
    (with-redefs
      [game-state (atom {:p0 :human})]
      
      (set-player 0 :computer)

      (should=
        (@game-state :p0)
        :computer)))
  (it "sets p0 to :human"
    (with-redefs
      [game-state (atom {:p0 :computer})]
      
      (set-player 0 :human)

      (should=
        (@game-state :p0)
        :human)))
  (it "sets p1 to :computer"
    (with-redefs
      [game-state (atom {:p1 :human})]
      
      (set-player 1 :computer)

      (should=
        (@game-state :p1)
        :computer)))

  (it "calls draw-page with state"
    (with-redefs [draw-page (stub :draw-page)]
      (set-player 1 :computer)

      (should-have-invoked :draw-page {:with [game-state]}))))
