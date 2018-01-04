(ns cljs-ttt.core
  (:require [cljs-ttt.computer-player :refer [get-negamax-move]])) 

(def body (.-body js/document))

;(get-negamax-move ["X" "O" " " "O" "X" " " "X" " " "O"])
(get-negamax-move ["X" "O" " " "X" "O" " " " " " " " "])
;(get-negamax-move ["X" "O" " " "X" "O" " " " " " " " "])
