(ns ^:figwheel-always hemmvm.cugb.electron-intro.handler
  (:require
   [re-frame.core :refer [register-handler dispatch]]
   [hemmvm.cugb.electron-intro.tree :as tree]
   [hemmvm.cugb.electron-intro.fs :as fs]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper

(defn db->buffers-dirty
  [db]
  (->> (vals (:buffers db))
       (filter :dirty)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Handlers

(register-handler :dir/open
  (fn [_ [_ root]]
    {:buffers {}
     :tree (tree/new-tree root)}))


(register-handler :node/edit
  (fn [db [_ path]]
    (when-not (get-in db [:buffers path])
      (dispatch [:buffer/load path]))

    (assoc db :path/current path)))


(register-handler :buffer/update
  (fn [db [_ path buffer]]
    (update-in db [:buffers path]
               (fnil merge {:path path})
               buffer)))


(register-handler :buffer/load
  (fn [db [_ path]]
    (fs/read! path #(dispatch [:buffer/update path {:content %}]))
    db))


(register-handler :buffers/save
  (fn [db _]
    (doseq [{:keys [path content]}
            (db->buffers-dirty db)]

      (->> #(dispatch [:buffer/update path {:dirty nil}])
           (fs/write! path content)))

    db))


(register-handler :buffers/reset
  (fn [db _]
    (doseq [{:keys [path]}
            (db->buffers-dirty db)]

      (->> #(dispatch [:buffer/update path {:content % :dirty nil}])
           (fs/read! path)))

    db))
