(ns beast-watch.events
  (:require
   [beast-watch.db :refer [default-value]]
   [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx path trim-v after debug]]
   [cljs.spec.alpha :as s]))

(reg-event-fx
 :initialise-db
 []
 (fn [{:keys [db]} _]
   {:db default-value}))

(reg-event-db
 :set-watching
 []
 (fn [db [_ new-filter-kw]]
   (assoc db :watching new-filter-kw)))
 
