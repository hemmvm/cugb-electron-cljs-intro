(ns ^:figwheel-always hemmvm.cugb.electron-intro.subscribe
  (:require-macros
   [reagent.ratom :refer [reaction]])

  (:require
   [clojure.walk :as walk]
   [re-frame.core :refer [register-sub subscribe]]))


(register-sub :tree?
  (fn [!db _]
    (reaction
     (boolean (:tree @!db)))))


(register-sub :path/current
  (fn [!db _]
    (reaction
     (@!db :path/current ::none))))


(register-sub :buffers
  (fn [!db _]
    (reaction
     (:buffers @!db))))


(register-sub :buffer/dirty?
  (fn [_ _]
    (let [!buffers (subscribe [:buffers])
          xform (comp (filter :dirty) (map :path))]
      (reaction
       (into #{} xform (vals @!buffers))))))


(register-sub :buffer/current
  (fn [_ _]
    (let [!path (subscribe [:path/current])
          !buffers (subscribe [:buffers])]
      (reaction
       (get @!buffers @!path)))))


(register-sub :tree/plain
  (fn [!db _]
    (reaction
     (:tree @!db))))


(register-sub :tree
  (fn [_ _]
    (let [!tree (subscribe [:tree/plain])
          !path (subscribe [:path/current])
          !dirty? (subscribe [:buffer/dirty?])]

      (reaction
       (->> @!tree
            (walk/postwalk (fn [{:keys [path] :as node}]
                             (cond-> node
                               (@!dirty? path) (assoc :dirty true)
                               (= @!path path) (assoc :active true)))))))))
