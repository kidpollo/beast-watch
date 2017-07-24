(ns beast-watch.views
  (:require [reagent.core  :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]
            [dommy.core :as dommy]))
(defn feed
  []
  (let [f @(subscribe [:video-feed])]
    [:div.card
     [:div.card-image
      [:figure {:class "image is-16by9"} 
       [:img {:src (:url f)}]]]
     [:div.card-content
      [:div.media-content
       [:p (:name f)]]]]))

(defn simulator
  [tags detection-areas]
  (reagent/create-class
     {:reagent-render
      (fn [] [:div.card
              [:div.card-image
               [:svg {:id "watch-area" :width "100%" :height 500}]]])
      :component-did-mount
      (fn [] 
        (let [room-width (.-clientWidth (dommy/sel1 "#watch-area"))
              room-height (.-clientHeight (dommy/sel1 "#watch-area"))
              svg (.. js/d3 (select "svg"))]
          (dispatch [:set-room-size {:width room-width :height room-height}])
          (doseq [[_ area] detection-areas]
            (.. svg
                (append "rect")
                (attr "id" (:id area))
                (attr "width" (:width area))
                (attr "height" (:height area))
                (attr "stroke" "tomato")
                (attr "stroke-width" "2px")
                (attr "fill-opacity" "0.8")
                (attr "fill" "none")
                (attr "x" (:pos-x area))
                (attr "y" (:pos-y area)) 
                ))
          (doseq [[_ tag] tags]
            (.. svg 
                (append "svn:image")
                (attr "width" 30)
                (attr "height" 30)
                (attr "xlink:href" (get-in tag [:beast :emoji-url]))
                (attr "id" (:id tag))
                (attr "x" (get-in tag [:beast :pos-x]))
                (attr "y" (get-in tag [:beast :pos-y]))))
          ))
      :component-did-update
      (fn [this]
        (let [[_ tags detection-areas] (reagent/argv this)
              svg (.. js/d3 (select "svg"))]
          (doseq [[_ tag] tags]
            (let [beast (.. svg
                           (select (str "#" (:id tag))))]
              (.. beast
                  (attr "x" (get-in tag [:beast :pos-x]))
                  (attr "y" (get-in tag [:beast :pos-y])))
              (.. svg
                  (selectAll "rect")
                  (attr "fill"
                        (fn []
                          (this-as this 
                            (let [detection-area (get detection-areas (.-id this) )
                                  beast-box (.. beast
                                                (node)
                                                (getBBox))
                                  beast-left (.-x beast-box)
                                  beast-right (+ beast-left (.-width beast-box))
                                  beast-top (.-y beast-box)
                                  beast-bottom (+ beast-top (.-height beast-box))
                                  area-box (.getBBox this)
                                  area-left (.-x area-box)
                                  area-right (+ area-left (.-width area-box))
                                  area-top (.-y area-box)
                                  area-bottom (+ area-top (.-height area-box))
                                  collide-x (and (< beast-left area-right) (> beast-right area-left))
                                  collide-y (and (< beast-top area-bottom) (> beast-bottom area-top))]
                              (if (and collide-x collide-y)
                                (do
                                  (when-not (get-in detection-area [:tags (:id tag)])
                                    (dispatch [:detection-area-activated tag detection-area]))
                                  "none")
                                "none")
                              )))))))
          ))}))

(defn beast-whatch-app
  []
  (let [tags (subscribe [:tags])
        detection-areas (subscribe [:detection-areas])
        beasts (subscribe [:beasts])] 
    (fn []
      [:div
       [:section#beastapp {:class "hero is-info"}
        [:div.hero-head
         [:div.container {:class "has-text-centered"}
          [:h1.title "Beast Watch"]
          [:h2.subtitle
           "Currently watched beast name"]]]
        [:div.hero-body 
         [:div.container
          [:div.columns
           [:div {:class "column is-one-quarter"}]
           [:div {:class "column"}
            [feed]]
           [:div {:class "column is-one-quarter"}]]]]]
       
       [:section#simulation {:class "section"}
        [:div.container {:class "has-text-centered"}
         [:h1.title "Simulator"]
         [:div.container
          [:div.columns
           [:div {:class "column is-one-quarter"}
            [:h2.subtitle "Click to Watch"]
            (for [[_ beast] @beasts]
              ^{:key (:id beast)}
              [:field
               [:control
                [:button.button {:on-click #(dispatch [:set-watching beast])}
                 (:name beast)]]])
            #_[:button {:on-click #(dispatch [:tick])}
             "Tick"]]
           [:div {:class "column"}
            [simulator @tags @detection-areas]]
           [:div {:class "column is-one-quarter"}]]]]]])))

