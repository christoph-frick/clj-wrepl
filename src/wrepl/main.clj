(ns wrepl.main
  (:require [wrepl.cli])
  (:gen-class))

(defn -main
  [& args]
  (wrepl.cli/main args))
