(set-env!
 :source-paths #{"src/frontend" "src/backend"}
 :resource-paths #{"resources"}
 :dependencies '[[adzerk/boot-cljs "2.1.0-SNAPSHOT" :scope "test"]
                 [powerlaces/boot-figreload "0.1.1-SNAPSHOT" :scope "test"]
                 [pandeiro/boot-http "0.7.6" :scope "test"]

                 ;; Dirac and cljs-devtoos
                 [binaryage/dirac "RELEASE" :scope "test"]
                 [binaryage/devtools "RELEASE" :scope "test"]
                 [powerlaces/boot-cljs-devtools "0.2.0" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [com.cemerick/piggieback "0.2.1"  :scope "test"]
                 [weasel "0.7.0"  :scope "test"]
                 ;; Has to be `0.2.13` for some reson https://github.com/boot-clj/boot-figreload
                 [org.clojure/tools.nrepl "0.2.13" :scope "test"]

                 ;; App deps

                 ;; Client
                 [org.clojure/clojurescript "1.9.671"]
                 [prismatic/dommy "1.1.0"]
                 [reagent "0.6.0-rc"]
                 [re-frame "0.9.4"]
                 [secretary "1.2.3"]])

(require '[adzerk.boot-cljs              :refer [cljs]]
         '[adzerk.boot-cljs-repl         :refer [cljs-repl]]
         '[powerlaces.boot-figreload     :refer [reload]]
         '[powerlaces.boot-cljs-devtools :refer [dirac cljs-devtools]]
         '[pandeiro.boot-http            :refer [serve]])

(deftask dev [D with-dirac bool "Enable Dirac Devtools."]
  (comp (serve :dir "resources/")
     (watch)
     (cljs-devtools)
     (reload)
     (if-not with-dirac
       (cljs-repl)
       (dirac))
     (cljs :source-map true
           :optimizations :none
           :compiler-options {:external-config
                              {:devtools/config {:features-to-install    [:formatters :hints]
                                                 :fn-symbol              "λ"
                                                 :print-config-overrides true}}})))

(deftask build-dev []
  (cljs :optimizations :none
        :source-map true))

(deftask build []
  (cljs :optimizations :advanced
        :source-map false
        :pretty-print false))

