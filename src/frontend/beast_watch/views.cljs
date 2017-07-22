(ns beast-watch.views
  (:require [reagent.core  :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]))

(defn beast-whatch-app
  []
  [:div
   [:section#beastapp {:class "hero is-info"}
    [:div.hero-head
     [:div.container {:class "has-text-centered"}
      [:h1.title "Beast Watch"]
      [:h2.subtitle
       "Currently watched beast name"]]]
    [:div.hero-body 
     [:div.container {:class "has-text-centered"}
      [:div.columns
       [:div {:class "column is-one-quarter"}]
       [:div {:class "column"}
        [:div.card
         [:div.card-image
          [:figure {:class "image is-16by9"}
           [:img {:src "https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg"}]]]]]
       [:div {:class "column is-one-quarter"}]]]]]

   [:section#simulation {:class "section"}
    [:div.container {:class "has-text-centered"}
     [:h1.title "Simulator"]
     ]]])

