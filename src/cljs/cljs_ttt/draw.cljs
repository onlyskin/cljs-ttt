(ns cljs-ttt.draw)

(defn clear-element [element]
  (aset element "innerHTML" ""))

(defn- add-cell-text [cell cell-value]
  (let [cell-text (.createElement js/document "div")]
    (.add (.-classList cell-text) "cell-text")
    (aset cell-text "textContent" (str cell-value))
    (.appendChild cell cell-text)))

(defn- make-cell [config row-index cell-index cell-value]
  (let [cell (.createElement js/document "div")]
    (.add (.-classList cell) "board-cell")
    (add-cell-text cell cell-value)
    (aset cell "onclick" #((config :cell-click) row-index cell-index))
    cell))

(defn- make-row [config row-index values]
  (let [row (.createElement js/document "div")
        cells (map-indexed #(make-cell config row-index %1 %2) values)]
    (.add (.-classList row) "row")
    (doseq [cell cells]
      (.appendChild row cell))
    row))

(defn- make-rows [board config]
  (->> board
       (partition 3)
       (map-indexed #(make-row config %1 %2))))

(defn- draw-grid [root game-state config]
  (let [rows (make-rows (@game-state :board) config)]
    (doseq [row rows]
      (.appendChild root row)))
  )

(defn draw-page [root game-state config]
  (clear-element root)
  (draw-grid root game-state config))
