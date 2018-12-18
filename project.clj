(defproject net.ofnir/wrepl "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [integrant "0.7.0"]
                 [org.clojure/tools.cli "0.4.1"]]
  :main wrepl.main
  :aot [wrepl.main])
