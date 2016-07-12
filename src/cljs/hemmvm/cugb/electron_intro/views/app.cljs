(ns hemmvm.cugb.electron-intro.views.app
  (:require
   [re-frame.core :refer [subscribe]]
   [hemmvm.cugb.electron-intro.views.tree :as tree]
   [hemmvm.cugb.electron-intro.views.editor :as editor]))


(defn hint
  []
  [:div.hint
   "Please choose a root directory by pressing Ctrl-o"])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; View

(defn view
  []
  (let [!tree? (subscribe [:tree?])]
    (fn []
      [:div.app
       [:div.app__side-pane
        [tree/view]]

       [:div.app__main-pane
        (if @!tree?
          [editor/view]
          [hint])]])))
