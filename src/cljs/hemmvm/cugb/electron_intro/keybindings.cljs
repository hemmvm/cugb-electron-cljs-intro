(ns hemmvm.cugb.electron-intro.keybindings
  (:require
   [re-frame.core :refer [dispatch]]
   [hemmvm.cugb.electron-intro.dialog :as dialog]))


;; HACK
(defn install!
  []
  (aset js/document "onkeydown"
        #(when (.-ctrlKey %)
           (case (.-code %)
             "KeyS" (dispatch [:buffers/save])
             "KeyR" (dispatch [:buffers/reset])
             "KeyO" (dispatch [:dir/open (dialog/open-dir)])

             nil))))
