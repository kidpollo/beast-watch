(ns beast-watch.db
  (:require [cljs.reader]
            [cljs.spec.alpha :as s]
            [re-frame.core :as re-frame]))

;; TODO: change to uuid?
(s/def :entity/id string?)

;; beasts
(s/def :beast/name string?)
(s/def :beast/pos-x int?)
(s/def :beast/pos-y int?)
(s/def :beast/move-x int?)
(s/def :beast/move-y int?)
(s/def :beast/emoji-url string?)
(s/def ::beast
  (s/keys :req-un [:entity/id :beast/name :beast/pos-x :beast/pos-y :beast/move-x :beast/move-y :beast/emoji-url]))
(s/def ::beasts
  (s/map-of :entity/id ::beast))
;; tags
(s/def :tag/beast
  ::beast)
(s/def ::tag
  (s/keys :req-un [:entity/id]
          :opt-un [:tag/beast]))
(s/def ::tags
  (s/map-of :entity/id ::tag))
;; video-feeds
(s/def :video-feed/name string?)
(s/def :video-feed/url string?)
(s/def ::video-feed
  (s/keys :req-un [:entity/id :video-feed/name :video-feed/url]))
(s/def ::video-feeds
  (s/map-of :entity/id ::video-feed))
;; detection-areas
(s/def :detection-area/width int?)
(s/def :detection-area/height int?)
(s/def :detection-area/pos-x int?)
(s/def :detection-area/pos-y int?)
(s/def :detection-area/tags (s/map-of :entity/id ::tag))
(s/def :detection-area/feed ::video-feed)
(s/def ::detection-area
  (s/keys :req-un [:entity/id :detection-area/width :detection-area/height
                   :detection-area/pos-x :detection-area/pos-y]
          :opt-un [:detection-area/tags
                   :detection-areas/feed]))
(s/def ::detection-areas
  (s/map-of :entity/id ::detection-area))

(s/def ::watching
  (s/or :none
        ::beast))

(s/def ::db (s/keys :req-un [::watching ::beasts ::detection-areas ::video-feed ::tags]))

(def default-no-feed
  {:id "feed-0" :name "no feed" :url "http://www.forgetthebox.net/wp-content/uploads/2016/08/no-rob-ford-crack-video.jpg"})

(def default-feeds
  (sorted-map "feed-0" default-no-feed
              "feed-1" {:id "feed-1" :name "room1" :url "https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg"}
              "feed-2" {:id "feed-2" :name "room2" :url "http://footage.framepool.com/shotimg/967107410-labrador-tier-panting-cosiness-dwelling.jpg"}))

(def default-detection-areas
  (sorted-map "area-1" {:id "area-1" :width 200 :height 400 :pos-x 50 :pos-y 50 :tags {}
                        :feed (get default-feeds "feed-1")}
              "area-2" {:id "area-2" :width 200 :height 400 :pos-x 400 :pos-y 50 :tags {}
                        :feed (get default-feeds "feed-2")}))

(def default-beasts
  (sorted-map "beast-1" {:id "beast-1" :name "rusty" :pos-x 50 :pos-y 100 :move-x 5 :move-y 1
                         :emoji-url "https://www.emojibase.com/resources/img/emojis/hangouts/1f408.png"}
              "beast-2" {:id "beast-2" :name "fido" :pos-x 50 :pos-y 200 :move-x -7 :move-y -2
                         :emoji-url "http://et-38d7.kxcdn.com/emojione-512/1f415.png"}))

; default db
(def default-value
  {:watching :none
   :beasts default-beasts
   :detection-areas default-detection-areas
   :video-feed default-no-feed
   :tags (sorted-map
          "tag-1" {:id "tag-1" :beast (get default-beasts "beast-1")}
          "tag-2" {:id "tag-2" :beast (get default-beasts "beast-2")}
          )
   :room-size {:width 600 :height 500}})


(comment

  (s/valid? ::id 1000)

  (s/valid? ::tag {:id 1})

  (s/valid? ::beast {:id "beast-1" :name "rusty"})

  (s/valid? ::beasts {"beast-1" {:id "beast-1" :name "rusty"}
                      "beast-2" {:id "beast-2" :name "fido"}
                      "beast-3" {:id "beast-3" :name "pooch"}})

  (s/valid? ::tag {:id "tag-1"})

  (s/valid? ::tag {:id "tag-1" :beast {:id "beast-1" :name "rusty"}})

  (s/valid? ::video-feed default-no-feed)

  (s/valid? ::video-feeds default-feeds)

  (s/valid? ::detection-areas default-detection-areas)

  (s/valid? ::detection-areas (assoc-in default-detection-areas
                                        ["area-1" :tags]
                                        {"tag-1" {:id "tag-1"}}))




  )
