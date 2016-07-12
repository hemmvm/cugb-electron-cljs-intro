(ns hemmvm.cugb.electron-intro.dialog)


(def dialog
  (.. (js/require "electron") -remote -dialog))


(defn open-dir
  []
  (let [opts (clj->js {:properties ["openDirectory"]})]
    (first (.showOpenDialog dialog opts))))
