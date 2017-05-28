(ns wrepl.repl
  (:require [clojure.main]
            [clojure.spec.alpha :as spec]))

; TODO: put the specs somewhere sane -- or decide agsinst using them at all and go with some simple checks instead

(spec/def :wrepl/init fn?)

(spec/def :wrepl/print fn?)

(spec/def :wrepl/system
  (spec/keys :req [:wrepl/init :wrepl/print]))


(defn repl
  [{:wrepl/keys [init print] :as system}]
  (spec/assert :wrepl/system system) ; TODO: does not fail right now
  (apply clojure.main/repl [:init init
                            :print print]))
