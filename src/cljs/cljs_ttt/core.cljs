(ns cljs-ttt.core
  (:require cljsjs.mithril))

(def body (.-body js/document))
(def page (js-obj "view" (fn [] (js/m "p" "Hello World"))))

(.mount js/m body page)
