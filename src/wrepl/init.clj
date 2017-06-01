(ns wrepl.init
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :wrepl.init/in-ns [_ {ns :ns :as opts}]
  (fn wrepl-init []
    (in-ns ns)))
