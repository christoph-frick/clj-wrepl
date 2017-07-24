(ns wrepl.init
  (:require [integrant.core :as ig]))


(defmethod ig/init-key :wrepl.init/in-ns [_ {ns :ns :as opts}]
  (fn wrepl-init []
    (in-ns ns)
    (clojure.core/use 'clojure.core)
    (use 'clojure.repl)
    ns))


(defmethod ig/init-key :wrepl.init/load-file [_ {filename :filename :as opts}]
  (fn wrepl-load-file []
    (load-file filename)))


(defmethod ig/init-key :wrepl.init/eval [_ {expr :expr :as opts}]
  (fn wrepl-eval []
    (let [sexp (read-string expr)]
      (print "; ")
      (prn sexp)
      (println (eval sexp)))))
