(ns hemmvm.cugb.electron-intro.views.tree
  (:require
   [plumbing.core :refer-macros [defnk]]
   [re-frame.core :refer [subscribe dispatch]]
   [hemmvm.cugb.electron-intro.util :as u]))


(declare node-view)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Plumbing: File

(defnk attrs-file
  [path {dirty nil} {active nil}]
  {:class
   (u/format "dirty-%s active-%s" dirty active)
   :on-click
   #(dispatch [:node/edit path])})

(defnk file-view
  [name :as node]
  [:div.node.node--file
   [:div.node__label (attrs-file node)
    name]])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Plumbing: Dir

(defnk dir-view
  [name children]
  [:div.node.node--dir
   [:div.node__label
    [:i.fa.fa-folder-open-o]
    name]

   (for [child children]
     ^{:key (:path child)} [node-view child])])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Plumbing: Node

(defn node-view
  [node]
  (case (:kind node)
    :dir (dir-view node)
    :file (file-view node)
    nil))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Porcelain

(defn view
  []
  (let [!tree (subscribe [:tree])]
    (fn []
      [:div.tree
       [node-view @!tree]])))
