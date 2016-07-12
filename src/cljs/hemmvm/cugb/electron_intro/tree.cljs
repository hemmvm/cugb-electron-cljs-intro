(ns hemmvm.cugb.electron-intro.tree
  (:require
   [clojure.walk :as walk]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Node modules

(def directory-tree
  (js/require "directory-tree"))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tree

(defn groom
  [node]
  (cond-> node
    (map? node)
    (-> (walk/keywordize-keys)
        (assoc :kind (if (get node "children") :dir :file))
        (select-keys [:path :name :kind :children]))))


(defn new-tree
  [root]
  (->> (directory-tree root)
       (js->clj)
       (walk/postwalk groom)))
