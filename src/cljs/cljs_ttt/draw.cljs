(ns cljs-ttt.draw)

(defn- make-cell [position]
  (.createElement js/document "button"))

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
