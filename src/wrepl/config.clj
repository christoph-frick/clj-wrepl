(ns wrepl.config
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [clojure.string :as str]))


(def ^:const default-config
  {:wrepl/init [(ig/ref :wrepl.init/in-ns) (ig/ref :wrepl/append-init)]
   :wrepl.init/in-ns {:ns 'user}
   :wrepl/eval (ig/ref :wrepl.eval/interruptible)
   :wrepl.eval/interruptible {}
   [:wrepl/append-init :wrepl/init] []})


(defmulti load-resource (fn [type _] type))

(defmethod load-resource :file 
  [_ source]
  (io/file source))

(defmethod load-resource :resource
  [_ source]
  (io/resource source))


(defn load-config
  [type resource]
  (-> (load-resource type resource)
      (slurp)
      (ig/read-string)))


(def ^:dynamic *default-base-name* "wrepl")


(def ^:dynamic *default-config-locations* {".$BASENAME.edn" :file
                                           ".wrepl/$BASENAME.edn" :file
                                           "$HOME/.$BASENAME.edn" :file
                                           "$HOME/.wrepl/$BASENAME.edn" :file
                                           "$BASENAME.edn" :resource})


(defn load-user-config
  ([]
   (load-user-config *default-base-name* *default-config-locations*))
  ([base-name locations]
   (let [replacements {"$HOME" (System/getProperty "user.home")
                       "$BASENAME" base-name}
         replace-fn (fn [s] (reduce-kv str/replace s replacements))
         load-fn (fn [source type] (let [source (replace-fn source)
                                         name (str (name type) "://" source)]
                                     (try
                                       (let [config (load-config type source)]
                                         (println "User config from" name)
                                         config)
                                       (catch Exception e
                                         #_(println "No config at" name ":" (.getMessage e))))))]
     (loop [[[source type] & rest] locations]
       (if-let [config (load-fn source type)]
         config
         (recur rest))))))


(defn append-init
  [config key cfg]
  (-> config
      (assoc key cfg)
      (update :wrepl/init (fnil conj []) (ig/ref key))))


(defmethod ig/init-key :wrepl/repl [_ f] f)
(defmethod ig/init-key :wrepl/init [_ fs]
  (fn init []
    (doseq [f fs] (f))))
(defmethod ig/init-key :wrepl/print [_ f] f)
(defmethod ig/init-key :wrepl/prompt [_ f] f)
(defmethod ig/init-key :wrepl/read [_ f] f)
(defmethod ig/init-key :wrepl/eval [_ f] f)
