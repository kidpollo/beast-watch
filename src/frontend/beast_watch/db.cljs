(ns beast-watch.db
  (:require [cljs.reader]
            [cljs.spec.alpha :as s]
            [re-frame.core :as re-frame]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::tag
  (s/keys :req-un [::id]))

(s/def ::beast
  (s/keys :req-un [::id ::name ::tag]))

(s/def ::beasts
  (s/map-of ::id ::beast))

(s/def ::watching
  (s/or :none
        ::beast))

(s/def ::db (s/keys :req-un [::todos ::showing]))

(def default-value
  {:watching :none
   :beasts (sorted-map)})


(comment

  (s/valid? ::id 1000)

  (s/valid? ::tag {:id 1})

  (s/valid? ::beast {:id 1 :name "rusty" :tag {:id 1}})

  (s/valid? ::beasts {1 {:id 1 :name "rusty" :tag {:id 1}}
                      2 {:id 2 :name "fido" :tag {:id 2}}
                      3 {:id 3 :name "pooch" :tag {:id 3}}})






  )
