(ns cljs-ttt.draw)

(defn- make-cell []
  (let [cell (.createElement js/document "button")]
    (aset cell "innerHTML" "Testing")
    ;(.add "board-cell"(aget cell "classList"))
    cell))

(defn draw-page [root game-state]
  (aset root "innerHTML" "")
  (->> (@game-state :board)
       (map make-cell)
       (map #(.appendChild root %)))
  )
