(ns wrepl.eval
  (:require [integrant.core :as ig]))

; stolen from: https://github.com/bhauman/rebel-readline/issues/180#issuecomment-429057767
(defn handle-sigint-form []
  `(let [thread# (Thread/currentThread)]
     (clojure.repl/set-break-handler! (fn [signal#] (.stop thread#)))))

(defmethod ig/init-key :wrepl.eval/interruptible [_ _]
  (fn wrepl-eval-interruptable [form]
    (eval
      `(do
         ~(handle-sigint-form)
         ~form))))
