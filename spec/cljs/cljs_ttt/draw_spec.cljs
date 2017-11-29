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
            (.-onclick))))))

(describe "empty-game?"
  (it "is true if game-state board is empty"
    (let [game-state (atom {:board (vec-for-string "         ")})]
      (should=
        true
        (empty-game? game-state))))

  (it "is false if game-state board not empty"
    (let [game-state (atom {:board (vec-for-string "XOX      ")})]
      (should=
        false
        (empty-game? game-state)))))
