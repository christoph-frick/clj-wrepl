(defproject wrepl "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-beta1"]
                 [integrant "0.6.1"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main wrepl.main
  :aot [wrepl.main])
