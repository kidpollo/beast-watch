(ns ^:figwheel-load beast-watch.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [dommy.core :as dommy]
            [beast-watch.events]
            [beast-watch.subs]
            [beast-watch.views :refer [beast-whatch-app]]
            [secretary.core :as secretary])
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)

(defroute "/" [] (dispatch [:set-showing :all]))

(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event] (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn main
  []
  (dispatch-sync [:initialise-db])
  (reagent/render [beast-whatch-app]
                  (.getElementById js/document "app")
                  #_(dommy/sel1 "#app")))