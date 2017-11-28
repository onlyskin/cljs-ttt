(ns cljs-ttt.draw)

(defn clear-element [element]
  (aset element "innerHTML" ""))

(defn- make-cell [position]
  (let [cell (.createElement js/document "div")]
    (.add (.-classList cell) "board-cell")
    (aset cell "textContent" (str position))
    cell))

(defn- make-row [positions]
  (let [row (.createElement js/document "div")
        cells (map make-cell positions)]
    (.add (.-classList row) "row")
    (doseq [cell cells]
      (.appendChild row cell))
    row))

(defn- make-rows [board]
  (->> board
       (partition 3)
       (map make-row)))

(defn- draw-grid [root game-state]
  (let [rows (make-rows (@game-state :board))]
    (doseq [row rows]
      (.appendChild root row)))
  )

(defn draw-page [root game-state]
  (clear-element root)
  (draw-grid root game-state)
  )
