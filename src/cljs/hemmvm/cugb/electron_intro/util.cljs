(ns hemmvm.cugb.electron-intro.util
  (:require
   [goog.string]
   [goog.string.format]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Strings

(defn format
  [fmt & args]
  (apply goog.string.format fmt args))
