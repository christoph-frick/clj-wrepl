(defproject net.ofnir/wrepl "0.1.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [integrant "0.7.0"]
                 [com.cemerick/pomegranate "1.1.0"]
                 [org.slf4j/slf4j-simple "1.7.29"]
                 [org.clojure/tools.cli "0.4.2"]]
  :main wrepl.main
  :aot [wrepl.main])
