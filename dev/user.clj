(ns user
  (:require
   [com.stuartsierra.component :as c]
   [figwheel-sidecar.repl-api :as ra])

  (import
   java.lang.Runtime))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Configs

(def figwheel-config
  {:figwheel-options
   {:server-port
    3449

    :css-dirs
    ["app/css"]}

   :build-ids
   ["dev"]

   :all-builds
   [{:id "dev"
     :figwheel
     {:on-jsload
      "hemmvm.cugb.electron-intro.main/on-jsload"}

     :source-paths
     ["src/cljs" "env/dev/cljs"]

     :compiler
     {:main "hemmvm.cugb.electron-intro.main"
      :asset-path "js/p/out"
      :output-to "app/js/p/app.js"
      :output-dir "app/js/p/out" }}]})

(def sass-config
  {:executable-path "sass"
   :input-dir "src/sass"
   :output-dir "app/css"})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Component: Figwheel

(defrecord Figwheel []
  c/Lifecycle
  (start [component]
    (ra/start-figwheel! component)
    component)

  (stop [component]
    (ra/stop-figwheel!)
    component))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Component: SassWatcher

(defrecord SassWatcher [executable-path input-dir output-dir
                        sass-watcher-process]
  c/Lifecycle
  (start [component]
    (if sass-watcher-process
      component
      (do
        (println "Figwheel: Starting SASS watch process")
        (assoc component :sass-watcher-process
               (.exec (Runtime/getRuntime)
                      (str executable-path " --watch " input-dir ":" output-dir))))))

  (stop [component]
    (when-let [process (:sass-watcher-process component)]
      (println "Figwheel: Stopping SASS watch process")
      (.destroy process))

    component))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; System

(def fig-system
  (atom (c/system-map
          :figwheel
          (map->Figwheel figwheel-config)

          :sass
          (map->SassWatcher sass-config))))


(defn fig-start []
  (swap! fig-system c/start))


(defn fig-stop []
  (swap! fig-system c/stop))


(defn reload []
  (fig-stop)
  (fig-start))


(defn fig-repl []
  (ra/cljs-repl))


(defn fig-init
  []
  (fig-start)
  (fig-repl))
