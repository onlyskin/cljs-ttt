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
                                  config
                                  set-player
                                  game-state
                                  root-element
                                  current-p]]
            [cljs-ttt.draw :refer [draw-page]]
            [cljs-ttt.computer-player :refer [get-negamax-move]]
            ))

(defn- vec-for-string [board-string]
  (clojure.string/split board-string ""))

(describe "run"
  (with-stubs)

  (it "calls draw-page with state and config"
    (with-redefs [draw-page (stub :draw-page)]
      (run)

      (should-have-invoked
        :draw-page
        {:with [root-element game-state config]})))

  (it "inits game-state with empty board"
    (run)

    (should=
      (vec-for-string "         ")
      (@game-state :board))))

(describe "restart"
  (with-stubs)

  (it "calls draw-page with state and config"
    (with-redefs [draw-page (stub :draw-page)]
      (restart)

      (should-have-invoked
        :draw-page
        {:with [root-element game-state config]})))

  (it "sets game-state board back to an empty board"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XXO      ")})]
      (restart)

      (should=
        (vec-for-string "         ")
        (@game-state :board)))))

(describe "play"
  (with-stubs)

  (it "calls draw-page with state config"
    (with-redefs
      [draw-page (stub :draw-page)
       game-state (atom {:board (vec-for-string "         ")
                         :p0 :human
                         :p1 :human})]
      (play 0 2)

      (should-have-invoked
        :draw-page
        {:with [root-element game-state config]})))

  (it "plays on the board"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOX O    ")
                         :p0 :human
                         :p1 :human})]
      (play 2 1)

      (should=
        (vec-for-string "XOX O  X ")
        (@game-state :board))))

  (it "doesnt play on the board if move unavailable"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOX O    ")
                         :p0 :human
                         :p1 :human})]
      (play 0 1)

      (should=
        (vec-for-string "XOX O    ")
        (@game-state :board))))

  (it "plays computer move if the next player is a computer"
    (with-redefs
      [game-state (atom {:board (vec-for-string "X   O    ")
                         :p0 :human
                         :p1 :computer})
       get-negamax-move (stub :get-negamax-move {:return 2})]

      (play 0 2)

      (should=
        (@game-state :board)
        (vec-for-string "XOX O    ")))) 

  (it "doesn't play if there are no more moves"
    (with-redefs
      [game-state (atom {:board (vec-for-string "XOXXOOOX ")
                         :p0 :human
                         :p1 :computer})
       get-negamax-move (stub :get-negamax-move {:return 2})]

      (play 2 2)

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

  (it "calls draw-page with state and config"
    (with-redefs [draw-page (stub :draw-page)]
      (set-player 1 :computer)

      (should-have-invoked
        :draw-page
        {:with [root-element game-state config]}))))
