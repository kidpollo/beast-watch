(ns beast-watch.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 :watching
 (fn [db _]
   (:watching db)))

(reg-sub
 :beasts
 (fn [db _]
   (:beasts db)))

(reg-sub
 :tags
 (fn [db _]
   (:tags db)))

(reg-sub
 :detection-areas
 (fn [db _]
   (:detection-areas db)))

(reg-sub
 :room-size
 (fn [db _]
   (:room-size db)))

(reg-sub
 :video-feed
 (fn [db _]
   (:video-feed db)))
