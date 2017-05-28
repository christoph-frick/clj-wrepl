(ns wrepl.config
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]))

; TODO: make init by default a list of functions?  or provide a helper to build that?  see below
; {:wrepl/init [(ig/ref :wrepl.default/in-ns)]
;  :wrepl.init/in-ns {:ns 'user}

(def ^:const default-config
  {:wrepl/init (ig/ref :wrepl.default/init)
   :wrepl/print (ig/ref :wrepl.default/print)
   ; defaults
   :wrepl.default/init {}
   :wrepl.default/print {}})


(defn load-config
  [file]
  (-> file
      (slurp)
      (ig/read-string)))


(defn load-config-by-name
  [filename]
  (load-config (io/file filename)))


(def ^:const default-config-filename ".wrepl.edn")


(defn load-user-config
  ([]
   (load-user-config default-config-filename))
  ([filename]
   (when-let [$HOME (System/getProperty "user.home")]
     (load-config (io/file $HOME filename)))))

; for now just pass-thrus; add checks here later here -- or find a way to get rid of it?
(defmethod ig/init-key :wrepl/init [_ f] f)
(defmethod ig/init-key :wrepl/print [_ f] f)
