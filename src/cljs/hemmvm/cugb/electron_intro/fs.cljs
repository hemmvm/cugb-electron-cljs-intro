(ns hemmvm.cugb.electron-intro.fs)

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Node modules

(def fs
  (js/require "fs"))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; IO

(defn read!
  [path cb]
  (->> #(or %1 (cb %2))
       (.readFile fs path "utf-8")))


(defn write!
  [path content cb]
  (->> #(or %1 (cb %2))
       (.writeFile fs path content "utf-8")))
