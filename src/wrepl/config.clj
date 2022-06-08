(ns wrepl.config
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [wrepl.replacement :as r]))


(def ^:const default-config
  {:wrepl/init [(ig/ref :in-ns/user) (ig/ref :wrepl/append-init)]
   [:in-ns/user :wrepl.init/in-ns] {:ns 'user}
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


(def ^:dynamic *base-name* "wrepl")


(def ^:dynamic *config-locations* {".$BASENAME.edn" :file
                                           ".wrepl/$BASENAME.edn" :file
                                           "$HOME/.$BASENAME.edn" :file
                                           "$HOME/.wrepl/$BASENAME.edn" :file
                                           "$BASENAME.edn" :resource})


(defn load-user-config
  ([]
   (load-user-config *base-name* *config-locations*))
  ([base-name locations]
   (let [replacements  (assoc r/default-path-replacements
                              "$BASENAME" base-name)
         load-fn (fn [source type] (let [source (r/replace-all replacements source)]
                                     (try
                                       (let [config (load-config type source)]
                                         (println "User config from" (str (name type) "://" source))
                                         config)
                                       (catch Exception e
                                         #_(println "No config at" name ":" (.getMessage e))))))]
     (loop [[[source type] & rest] locations]
       (if-let [config (load-fn source type)]
         config
         (if (seq rest)
           (recur rest)
           (do
             (println (str "Found no config for `$BASENAME=" base-name "` in " (keys locations)))
             {})))))))


(defn append-init
  [config key cfg]
  (-> config
      (assoc key cfg)
      (update :wrepl/init (fnil conj []) (ig/ref key))))


(defmethod ig/init-key :wrepl/deps [_ f] f) ; this does not actually take part in integrant
(defmethod ig/init-key :wrepl/repl [_ f] f)
(defmethod ig/init-key :wrepl/init [_ fs]
  (fn init []
    (doseq [f fs] (f))))
(defmethod ig/init-key :wrepl/print [_ f] f)
(defmethod ig/init-key :wrepl/prompt [_ f] f)
(defmethod ig/init-key :wrepl/read [_ f] f)
(defmethod ig/init-key :wrepl/eval [_ f] f)
