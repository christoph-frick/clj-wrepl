(ns wrepl.repl
  (:require [clojure.main]
            [clojure.spec.alpha :as spec]))

; TODO: put the specs somewhere sane -- or decide agsinst using them at all and go with some simple checks instead

(spec/def :wrepl/init (spec/coll-of fn?))

(spec/def :wrepl/print fn?)

(spec/def :wrepl/prompt fn?)

(spec/def :wrepl/read fn?)

(spec/def :wrepl/system
  (spec/keys :opt [:wrepl/init :wrepl/print :wrepl/prompt :wrepl/read]))


(defn repl-params
  [system]
  (let [supported [:wrepl/init :wrepl/print :wrepl/prompt :wrepl/read]]
    (into [] (mapcat (fn [[k v]] [(keyword (name k)) v])) (select-keys system supported))))


(defn repl
  [system]
  (spec/assert :wrepl/system system) ; TODO: does not fail right now
  (apply clojure.main/repl (repl-params system)))
