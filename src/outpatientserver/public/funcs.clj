(ns outpatientserver.public.funcs
      (:use org.httpkit.server)
      (:use ruiyun.tools.timer)
  (:require
            [clojure.data.json :as json]
            [taoensso.timbre :as timbre]

            [ring.util.http-response :refer [ok]]
            )
)

;; clear duplate arr
(defn distinct-case-insensitive [xs]
  (->> xs
    (group-by clojure.string/lower-case)
    (vals)
    (map first)))


(def datapath (str (System/getProperty "user.dir") "/"))



