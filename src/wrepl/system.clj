(ns wrepl.system
  (:require [integrant.core :as ig]
            [cemerick.pomegranate :refer [add-dependencies]]))

(defn build-system
  "Load the namespaces referenced in the config, init the system, and return it"
  [config]
  ; deps are handled outside of integrant and before
  (when-let [{:wrepl/keys [deps]} config]
    (let [{:keys [coordinates repositories]
           :or {repositories {"clojars" "https://clojars.org/repo"
                              "jcenter" "https://jcenter.bintray.com"}}
           :as opts} deps]
      (when coordinates
        (add-dependencies :coordinates coordinates
                          :repositories repositories))))
  (ig/load-namespaces config)
  (ig/init config))
