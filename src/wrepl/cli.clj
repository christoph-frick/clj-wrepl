(ns wrepl.cli
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [wrepl.repl]
            [wrepl.config]
            [wrepl.system]))

(defn read-version
  []
  (let [fallback "dev"]
    (or
     (some-> "META-INF/leiningen/net.ofnir/wrepl/project.clj"
             (io/resource)
             (slurp)
             (edn/read-string)
             (nth 2))
     fallback)))

(defn- file-exists?
  [file-name]
  (.exists (io/file file-name)))

(defn- usage
  [summary]
  (str "WREPL " (read-version) "\n\nwrepl [options...]\n\n" summary))

(defn- error-message
  [usage errors]
  (str "ERRORS:\n\n" (str/join "\n" errors) "\n\n" usage))

(defn- exit
  [exit-code message]
  (println message)
  (System/exit exit-code))

(def ^:const cli-options
  [["-b" "--base-name base-name" "Base name for the user config to search for; e.g. $HOME/.wrepl/$BASENAME.edn (default: wrepl)"]
   ["-c" "--config config.edn" "Read the integrant system config from this file and merge it with the default"
    :validate [file-exists? "File must exist"]]
   [nil "--no-default-config" (str "Don't load the default config")]
   [nil "--no-user-config" (str "Don't load the default user config")]
   ["-i" "--init script.clj" "Run the given file before the first prompt"
    :validate [file-exists? "File must exist"]]
   ["-e" "--eval string" "Evaluate the expression (after --init if both given)"]
   ["-h" "--help"]
   ["-v" "--version"]])

(defn- merge-config
  [a b]
  (if b
    (merge a b)
    a))

(defn main
  [args]
  (let [{:keys [options errors summary] :as _opts} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (:version options) (exit 0 (read-version))
      errors (exit 1 (error-message (usage summary) errors)))
    (let [base-name (or (:base-name options) wrepl.config/*base-name*)]
      (binding [wrepl.config/*base-name* base-name]
        (let [config (cond-> {}
                       ; load the default config unless prohibited
                       (not (contains? options :no-default-config))
                       (merge-config wrepl.config/default-config )
                       ; load default user config unless prohibited
                       (not (contains? options :no-user-config))
                       (merge-config (wrepl.config/load-user-config))
                       ; load commandline provided config file
                       (contains? options :config)
                       (merge-config (wrepl.config/load-config :file (:config options)))
                       ; load-file user script
                       (contains? options :init)
                       (wrepl.config/append-init [:wrepl.main/load-file :wrepl.init/load-file] {:filename (:init options)})
                       ; eval user string
                       (contains? options :eval)
                       (wrepl.config/append-init [:wrepl.main/eval :wrepl.init/eval] {:expr (:eval options)}))
              system (wrepl.system/build-system config)]
          (wrepl.repl/repl system))))))
