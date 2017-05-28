(ns wrepl.main
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [wrepl.repl]
            [wrepl.config])
  (:gen-class))

(defn- file-exists?
  [file-name]
  (.exists (io/file file-name)))

(defn- usage
  [summary]
  (str "WREPL\n\nwrepl [options...]\n\n" summary))

(defn- error-message
  [usage errors]
  (str "ERRORS:\n\n" (str/join "\n" errors) "\n\n" usage))

(defn- exit
  [exit-code message]
  (println message)
  (System/exit exit-code))

(def ^:const cli-options
  [["-c" "--config config.edn" "Read the integrant system config from this file and merge it with the default"
    :validate [file-exists? "File must exist"]]
   [nil "--no-user-config" (str "Don't load the default user config from $HOME/" wrepl.config/default-config-filename)]
   ; TODO ["-i" "--init script.clj" "Run the given file before the first prompt"
   ;  :validate [file-exists? "File must exist"]]
   ; TODO ["-e" "--eval string" "Evaluate the expression (after --init if both given)"]
   ["-h" "--help"]])

(defn- merge-config
  [a b]
  (if b
    (merge a b)
    a))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary] :as opts} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-message (usage summary) errors)))
    (let [config (cond-> wrepl.config/default-config
                   (not (contains? options :no-user-config))
                   (merge-config (wrepl.config/load-user-config))
                   ;
                   (contains? options :config)
                   (merge-config (wrepl.config/load-config-by-name (:config options))))
          _ (ig/load-namespaces config)
          system (ig/init config)]
      (wrepl.repl/repl system))))
