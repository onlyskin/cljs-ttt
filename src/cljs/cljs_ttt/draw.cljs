(ns cljs-ttt.draw)

(defn- make-span [position]
  (let [span (.createElement js/document "span")]
    (aset span "textContent" (str position))
    span))

(defn- make-cell [position]
  (let [cell (.createElement js/document "button")]
    (.add (.-classList cell) "board-cell")
    ;(aset cell "textContent" (str position))
    (.appendChild cell (make-span position))
    cell))

(defn- make-cells [board]
  (map make-cell board))

(defn- draw-cells [root game-state]
  (let [cells (make-cells (@game-state :board))]
    (doseq [cell cells]
      (.appendChild root cell))))

(defn clear-element [element]
  (aset element "innerHTML" ""))

(defn draw-page [root game-state]
  (clear-element root)
  (draw-cells root game-state))
