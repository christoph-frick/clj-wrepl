(ns wrepl.default
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :wrepl.default/init [_ {:as opts}]
  (fn wrepl-init []
    (in-ns 'user)))

(defmethod ig/init-key :wrepl.default/print [_ {:as opts}]
  prn)
