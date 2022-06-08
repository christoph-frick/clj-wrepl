(ns wrepl.replacement-test
  (:require [clojure.test :refer [deftest testing is are]]
            [wrepl.replacement :as sut]))

(deftest test-replace-all
  (are [kvs s result] (= (sut/replace-all kvs s) result)
    nil nil nil
    nil "" ""
    {"x" "y"} nil nil
    {"$KEY" nil} "$KEY" ""
    {"$HOME" "/home/user"} "$HOME/file" "/home/user/file"))
