(defproject net.ofnir/wrepl "0.2.0"
  :description "Configuration of all the turning parts of a REPL using integrant"
  :url "https://github.com/christoph-frick/clj-wrepl"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [integrant "0.13.1"]
                 [org.slf4j/slf4j-simple "2.0.16"]
                 [org.clojure/tools.cli "1.1.230"]]
  :main wrepl.main
  :aot [wrepl.main]
  :profiles {:kaocha {:dependencies [[lambdaisland/kaocha "1.91.1392"]]}}
  :aliases {"test-refresh" ["kaocha" "--watch"]
            "kaocha" ["with-profile" "+kaocha" "run" "-m" "kaocha.runner"]}
  :global-vars {*warn-on-reflection* true
                *unchecked-math* true}
  :deploy-repositories {"clojars" {:url "https://clojars.org/repo/" :creds :gpg}}
  ; no release; binary gets built at github
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy" "clojars"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]])
