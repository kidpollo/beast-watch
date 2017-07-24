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
 (fn [db [_ beast]]
   (assoc db :watching beast)))

(reg-event-db
 :tick
 []
 (fn [db [_ _]]
   (let [room-width (get-in db [:room-size :width])
         room-height (get-in db [:room-size :height])]
     (-> db
         (update :tags
                 (fn [tags]
                   (->> tags
                        (map (fn [[tag-key tag]]
                               (let [x (get-in tag [:beast :pos-x])
                                     y (get-in tag [:beast :pos-y])
                                     speed-x (get-in tag [:beast :move-x])
                                     speed-y (get-in tag [:beast :move-y])
                                     move-x (if (or (> x room-width)
                                                    (< x 0))
                                              (* -1  speed-x)
                                              speed-x) 
                                     move-y (if (or (> y room-height)
                                                    (< y 0))
                                              (* -1  speed-y)
                                              speed-y)]
                                 [tag-key (-> tag
                                              (assoc-in [:beast :move-x] move-x)
                                              (assoc-in [:beast :move-y] move-y)
                                              (update-in [:beast :pos-x] + move-x)
                                              (update-in [:beast :pos-y] + move-y))])))
                        (into (sorted-map)))))
         ))))

(reg-event-db
 :set-room-size
 []
 (fn [db [_ new-size]]
   (-> db
       (assoc-in [:room-size :width] (:width new-size))
       (assoc-in [:room-size :height] (:height new-size)))))

(reg-event-db
 :detection-area-activated
 []
 (fn [db [_ tag area]]
   (-> db
       (cond->
           (= (get-in tag [:beast :id])
              (get-in db [:watching :id]))
         (assoc :video-feed (:feed area)))
       (update :detection-areas
               (fn [areas]
                 (->> areas
                      (map (fn [[key val]]
                             [key (update-in val [:tags] dissoc (:id tag))]))
                      (into (sorted-map)))))
       (assoc-in [:detection-areas (:id area) :tags (:id tag)] tag))))

(comment
  (def areas {"area-1" {:id "area-1", :width 200, :height 400, :pos-x 50, :pos-y 50, :tags {}}, "area-2" {:id "area-2", :width 200, :height 400, :pos-x 400, :pos-y 50, :tags {}}})

  (->> areas
       (map (fn [[key val]]
              [key (update-in val [:tags] dissoc (:id tag))]))
       (into (sorted-map)))



  )
