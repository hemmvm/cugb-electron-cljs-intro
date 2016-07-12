(ns hemmvm.cugb.electron-intro.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch]]

   [hemmvm.cugb.electron-intro.subscribe]
   [hemmvm.cugb.electron-intro.handler]

   [hemmvm.cugb.electron-intro.keybindings :as keybindings]
   [hemmvm.cugb.electron-intro.views.app :as app]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Mount

(def target
  (.getElementById js/document "app"))


(defn mount!
  []
  (reagent/render [app/view] target))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Init

(defn init!
  []
  (mount!)
  (keybindings/install!))
