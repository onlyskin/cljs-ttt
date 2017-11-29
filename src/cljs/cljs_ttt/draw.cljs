(ns cljs-ttt.draw
  (:require [cljs-ttt.board :refer [available-moves game-over?]]))

(defn- moves-count [board]
  (->> board
       (available-moves)
       (count)))

(defn empty-game? [game-state]
  (= 9 (moves-count (@game-state :board))))

(defn finished-game? [game-state]
  (= 0 (moves-count (@game-state :board))))

(defn in-progress-game? [game-state]
  (and
      (not= 9 (moves-count (@game-state :board)))
      (not= 0 (moves-count (@game-state :board)))))

(defn clear-element [element]
  (aset element "innerHTML" ""))

(defn- add-cell-text [cell cell-value]
  (let [cell-text (.createElement js/document "div")]
    (.add (.-classList cell-text) "cell-text")
    (aset cell-text "textContent" (str cell-value))
    (.appendChild cell cell-text)))

(defn- make-cell [board config row-index cell-index cell-value]
  (let [cell (.createElement js/document "div")]
    (.add (.-classList cell) "board-cell")
    (add-cell-text cell cell-value)
    (cond (not (game-over? board)) (aset cell "onclick" #((config :cell-click) row-index cell-index)))
    cell))

(defn- make-row [board config row-index values]
  (let [row (.createElement js/document "div")
        cells (map-indexed #(make-cell board config row-index %1 %2) values)]
    (.add (.-classList row) "row")
    (doseq [cell cells]
      (.appendChild row cell))
    row))

(defn- make-rows [board config]
  (->> board
       (partition 3)
       (map-indexed #(make-row board config %1 %2))))

(defn- make-grid-container []
  (let [container (.createElement js/document "div")]
    (.add (.-classList container) "container-fluid")
    container))

(defn- make-grid [container game-state config]
  (let [rows (make-rows (@game-state :board) config)]
    (doseq [row rows]
      (.appendChild container row)))
  container)

(defn- add-button [root index game-state config text player-type]
  (let [button (.createElement js/document "div")]
    (.add (.-classList button) "player-button")
    (aset button "textContent" text)
    (.appendChild root button)
    (cond
      (empty-game? game-state)
      (aset button "onclick" #((config :player-click) index player-type)))
    ) 
  )

(defn- add-button-pair [root index game-state config]
  (let [vertical-flex (.createElement js/document "div")]
    (.add (.-classList vertical-flex) "vertical-flex")
    (.appendChild root vertical-flex)
    (add-button vertical-flex index game-state config "Human" :human)
    (add-button vertical-flex index game-state config "Computer" :computer)))

(defn- add-button-container [root]
  (let [element (.createElement js/document "div")]
    (.add (.-classList element) "button-container")
    (.appendChild root element)
    element))

(defn- add-buttons [root game-state config]
  (let [button-container (add-button-container root)]
    (doseq [index (range 0 2)]
      (add-button-pair button-container index game-state config))))

(defn draw-page [root game-state config]
  (clear-element root)
  (let [grid-container (make-grid-container)]
    (make-grid grid-container game-state config)
    (.appendChild root grid-container))
  (add-buttons root game-state config)
  )
