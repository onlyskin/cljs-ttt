(ns cljs-ttt.core
  (:require [cljs-ttt.app :refer [run]]))

(def body (.-body js/document))

(run)
