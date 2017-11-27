(ns cljs-ttt.draw-spec 
  (:require-macros [speclj.core :refer [describe it should=]])
  (:require [speclj.core]
            [cljs-ttt.draw :refer [draw-page]]))

(defn- vec-for-string [board-string]
  (clojure.string/split board-string ""))

(def root (.createElement js/document "div"))

(describe "draw-page"
  (it "removes any html in the element"
    (let [game-state (atom {:board (vec-for-string "         ")})]
      (.appendChild root (.createElement js/document "p"))  

      (draw-page root game-state)

      (should= 0 (.. root -children -length))))

  (it "has a button"
    (let [game-state (atom {:board (vec-for-string " X O     ")})]

      (draw-page root game-state)

      (.log js/console root)
      (.log js/console "HERE")

      (should=
        9
        (.-length (.querySelectorAll root ".board-cell")))))
  )
