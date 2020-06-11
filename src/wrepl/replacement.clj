(ns wrepl.replacement
  (:require [clojure.string :as str]))

(def default-path-replacements
  {"$HOME" (System/getProperty "user.home")})

(defn replace-all
  "Replaces all keys with their value inside the string"
  [kvs s]
  (reduce-kv str/replace s kvs))
