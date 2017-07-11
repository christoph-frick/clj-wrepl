(ns wrepl.system
  (:require [integrant.core :as ig]))

(defn build-system
  "Load the namespaces referenced in the config, init the system, and return it"
  [config]
  (ig/load-namespaces config)
  (ig/init config))
