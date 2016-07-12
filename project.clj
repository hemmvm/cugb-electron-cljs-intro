(defproject hemmvm/cugb-electron-intro "0.1.0-SNAPSHOT"
  :description
  "Electron / ClojureScript Intro (CUGB - Clojure User Group Bonn)"

  :url
  "https://github.com/hemmvm/cugb-electron-cljs-intro"

  :source-paths
  ["src/cljs"]

  :dependencies
  [[org.clojure/clojure "1.7.0"]
   [org.clojure/clojurescript "1.8.51"]

   ;; Commons
   [com.stuartsierra/component "0.2.3"]
   [prismatic/plumbing "0.5.2"]

   ;; Node
   [cljsjs/nodejs-externs "1.0.4-1"]

   ;; ClojureScript
   [re-frame "0.7.0"]
   [reagent "0.6.0-rc"]]

  :plugins
  [[lein-cljsbuild "1.1.3"]]

  :cljsbuild
  {:builds
   [{:id "production"
     :source-paths
     ["src/cljs" "env/prod/cljs"]

     :compiler
     {:main
      "hemmvm.cugb.electron-intro.main"

      :asset-path
      "js/p/out"

      :output-to
      "app/js/p/app.js"

      :output-dir
      "app/js/p/out"

      :optimizations
      :advanced}}]}

  :clean-targets ^{:protect false}
  [:target-path "out" "app/js/p"]

  :profiles
  {:dev
   {:dependencies
    [[figwheel-sidecar "0.5.0-1"]
     [com.cemerick/piggieback "0.2.1"]]

    :source-paths
    ["dev"]}})
