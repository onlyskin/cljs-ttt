(ns cljs-ttt.draw-spec 
  (:require-macros [speclj.core :refer [describe
                                        it
                                        should=
                                        should-not=
                                        should-have-invoked
                                        stub
                                        with-stubs]])
  (:require [speclj.core]
            [cljs-ttt.draw :refer [clear-element
                                   draw-page
                                   empty-game?
                                   in-progress-game?
                                   finished-game?
                                   ]]))

(defn- vec-for-string [board-string]
  (clojure.string/split board-string ""))

(def root (.createElement js/document "div"))

(describe "draw-page"
  (with-stubs)

  (it "removes any html in the element"
    (.appendChild root (.createElement js/document "p"))  

    (clear-element root)

    (should= 0 (.. root -children -length)))

  (it "has three .row divs"
    (let [game-state (atom {:board (vec-for-string " X O     ")})]
      (draw-page root game-state {})

      (should=
        3
        (-> root
            (.querySelectorAll ".row")
            (.-length)))))

  (it "the .row divs have three .board-cells"
    (let [game-state (atom {:board (vec-for-string " X O     ")})]
      (draw-page root game-state {})

      (should=
        3
        (-> root
            (.querySelector ".row")
            (.querySelectorAll ".board-cell")
            (.-length)))

      (should=
        "X"
        (-> root
             (.querySelectorAll ".board-cell")
             (.item 1)
             (.querySelector ".cell-text")
             (.-textContent)))))

  (it "the .board-cells have event handlers attached"
    (let [game-state (atom {:board (vec-for-string " X O     ")})]
      (draw-page root game-state {:cell-click nil})
      
      (should-not=
        nil
        (-> root
            (.querySelector ".board-cell")
            (.-onclick)))))
  
  (it "there are four player-buttons when the game is not started"
    (let [game-state (atom {:board (vec-for-string "         ")
                            :p0 :human
                            :p1 :human})]
      (draw-page root game-state {:cell-click nil})

      (should=
        4
        (-> root
            (.querySelectorAll ".player-button")
            (.-length)))))

  ;(it "the first and 4th buttons have class highlight"
  ;  (let [game-state (atom {:board (vec-for-string "         ")
  ;                          :p0 :human
  ;                          :p1 :computer})]
  ;    (draw-page root game-state {:cell-click nil})

  ;    (should=
  ;      "player-button highlight"
  ;      (-> root
  ;          (.querySelectorAll ".player-button")
  ;          (.item 0)
  ;          (.-className))))

  ;    (should=
  ;      "player-button highlight"
  ;      (-> root
  ;          (.querySelectorAll ".player-button")
  ;          (.item 4)
  ;          (.-className))) 
  ;  )
  )

(describe "empty-game?"
  (it "is true if game-state board is empty"
    (let
      [game-state (atom {:board (vec-for-string "         ")})]
      (should=
        true
        (empty-game? game-state))))

  (it "is false if game-state board not empty"
    (let 
      [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        false
        (empty-game? game-state)))))

(describe "finished-game?"
  (it "is true if game-state board is full"
    (let
      [game-state (atom {:board (vec-for-string "XXOOOXXOX")})]
      (should=
        true
        (finished-game? game-state))))

  (it "is false if game-state board is not full"
    (let
      [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        false
        (finished-game? game-state)))))

(describe "in-progress-game?"
  (it "is false if game-state board is empty"
    (let
      [game-state (atom {:board (vec-for-string "         ")})]
      (should=
        false
        (in-progress-game? game-state))))

  (it "is true if game-state board has some moves"
    (let
      [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        true
        (in-progress-game? game-state))))

  (it "is false if game-state board is full"
    (let
      [game-state (atom {:board (vec-for-string "XXOOOXXOX")})]
      (should=
        false
        (in-progress-game? game-state)))))
