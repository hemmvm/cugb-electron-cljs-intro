(ns ^:figwheel-no-load hemmvm.cugb.electron-intro.main
  (:require
   [reagent.core :as reagent]
   [hemmvm.cugb.electron-intro.core :as core]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Convenience

(def electron
  (js/require "electron"))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Webframe zoom factor

(defn scale!
  [percent]
  (-> (.-webFrame electron)
      (.setZoomFactor (/ percent 100.0))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DevTools: Inspect Element

(defn inspect-element!
  [x y]
  (-> (.-remote electron)
      (.getCurrentWindow)
      (.inspectElement x y)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Handle right-clicks

(defn on-right-click
  [e]
  (.preventDefault e)
  (.stopPropagation e)

  (inspect-element! (.-x e) (.-y e)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Figwheel

(defn on-jsload
  []
  (reagent/unmount-component-at-node core/target)
  (core/mount!))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Init

(defonce _
  (-> js/window
      (.addEventListener "contextmenu" on-right-click)))


(core/init!)
