(ns cljs-ttt.draw-spec 
  (:require-macros [speclj.core :refer [describe it should=]])
  (:require [speclj.core]
            [cljs-ttt.draw :refer [clear-element draw-page]]))

(defn- vec-for-string [board-string]
  (clojure.string/split board-string ""))

(def root (.createElement js/document "div"))

(describe "draw-page"
  (it "removes any html in the element"
    (.appendChild root (.createElement js/document "p"))  

    (clear-element root)

    (should= 0 (.. root -children -length)))

  (it "has three .row divs"
    (let [game-state (atom {:board (vec-for-string " X O     ")})]
      (draw-page root game-state)

      (should=
        3
        (.-length (.querySelectorAll root ".row")))))

  (it "the .row divs have three .board-cells"
    (let [game-state (atom {:board (vec-for-string " X O     ")})]
      (draw-page root game-state)

      (should=
        3
        (.-length (.querySelectorAll (.querySelector root ".row") ".board-cell")))

      (should=
        "X"
        (.-textContent (.item (.querySelectorAll root ".board-cell") 1))))))
