(defproject net.ofnir/wrepl "0.1.3-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [integrant "0.8.0"]
                 [com.cemerick/pomegranate "1.1.0"]
                 [org.slf4j/slf4j-simple "1.7.36"]
                 [org.clojure/tools.cli "1.0.206"]]
  :main wrepl.main
  :aot [wrepl.main]
  ; no release; binary gets built at github
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]])
