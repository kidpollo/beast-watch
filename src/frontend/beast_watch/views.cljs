(ns beast-watch.views
  (:require [reagent.core  :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]))

(defn beast-whatch-app
  []
  [:div
   [:section#beastapp
    "Hola"]
   [:footer#info
    "Mundo!!"]])
