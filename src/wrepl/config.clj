(ns wrepl.config
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]))

(def ^:const default-config
  {:wrepl/init [(ig/ref :wrepl.init/in-ns) (ig/ref :wrepl/append-init)]
   :wrepl.init/in-ns {:ns 'user}
   [:wrepl/append-init :wrepl/init] []})


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


(defn append-init
  [config key cfg]
  (-> config
      (assoc key cfg)
      (update :wrepl/init (fnil conj []) (ig/ref key))))


(defmethod ig/init-key :wrepl/init [_ fs]
  (fn init []
    (doseq [f fs] (f))))
(defmethod ig/init-key :wrepl/print [_ f] f)
(defmethod ig/init-key :wrepl/prompt [_ f] f)
