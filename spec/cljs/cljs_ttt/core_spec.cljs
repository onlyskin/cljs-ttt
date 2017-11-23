(ns cljs-ttt.core-spec 
  (:require-macros [speclj.core :refer [describe it should=]])
  (:require [speclj.core]
            [cljs-ttt.core]))

(describe "A ClojureScript test"
  (it "fails. Fix it!"
    (should= 0 1)))
