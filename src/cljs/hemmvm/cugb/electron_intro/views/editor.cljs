(ns hemmvm.cugb.electron-intro.views.editor
  (:require
   [plumbing.core :refer-macros [defnk]]
   [re-frame.core :refer [subscribe dispatch]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Plumbing: Editor

(defnk attrs-textarea
  [path content]
  {:value
   content

   :on-change
   #(let [data {:content (.. % -target -value) :dirty true}]
      (dispatch [:buffer/update path data]))})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; View

(defn view
  []
  (let [!buffer (subscribe [:buffer/current])]
    (fn []
      [:div.editor
       (when @!buffer
         [:textarea (attrs-textarea @!buffer)])])))
