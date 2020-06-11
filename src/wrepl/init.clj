(ns wrepl.init
  (:require [integrant.core :as ig]
            [wrepl.replacement :as r]
            [clojure.main]))


(defmethod ig/init-key :wrepl.init/in-ns [_ {ns :ns :as opts}]
  (fn wrepl-init []
    (in-ns ns)
    (clojure.core/use 'clojure.core)
    (apply require clojure.main/repl-requires)
    ns))


(defmethod ig/init-key :wrepl.init/load-file [_ {filename :filename :as opts}]
  (fn wrepl-load-file []
    (load-file (r/replace-all r/default-path-replacements filename))))


(defmethod ig/init-key :wrepl.init/eval [_ {expr :expr :as opts}]
  (fn wrepl-eval []
    (let [sexp (read-string expr)]
      (print "; ")
      (prn sexp)
      (println (eval sexp)))))
