(defproject cljs-ttt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0-RC2"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [speclj "3.3.1"]]

  :profiles {:dev {:dependencies [[speclj "3.3.1"]]}}

  :plugins [[speclj "3.3.1"]
            [lein-cljsbuild "1.0.5"]]

  :cljsbuild {:builds        {:dev  {:source-paths   ["src/cljs" "spec/cljs"]
                                     :compiler       {:output-to "resources/public/js/compiled/main.js"
                                                      :output-dir "resources/public/js/compiled"
                                                      :optimizations :whitespace
                                                      :source-map "resources/public/js/compiled/main.js.map"
                                                      :pretty-print  true}
                                     :notify-command ["phantomjs"  "bin/speclj" "resources/public/js/compiled/main.js"]}

                              :prod {:source-paths ["src/cljs"]
                                     :compiler     {:output-to     "js/cljs-ttt.js"
                                                    :optimizations :simple}}}
              :test-commands {"test" ["phantomjs" "bin/speclj" "resources/public/js/compiled/main.js"]}}

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["spec/clj", "spec/cljs"]

  :aliases {"cljs" ["do" "clean," "cljsbuild" "once" "dev"]})
