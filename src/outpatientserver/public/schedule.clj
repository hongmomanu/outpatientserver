(ns outpatientserver.public.schedule
      (:use org.httpkit.server)
      (:use ruiyun.tools.timer)
  (:require
            [clojure.data.json :as json]
            [taoensso.timbre :as timbre]
            [outpatientserver.controller.home :as home]
            [ring.util.http-response :refer [ok]]
            )
)

(def schedule-timer (timer "The timer for schedule"))

(defn distinct-case-insensitive [xs]
  (->> xs
    (group-by clojure.string/lower-case)
    (vals)
    (map first)))

(defn start-schedule []

  (timbre/info "timer  schedule  started")
  (run-task! #(home/scheduleFunc ) :period 60000 :by schedule-timer)

  )
(defn stop-schedule[]
  (timbre/info "timer  schedule  stoped")
  (cancel! schedule-timer)
  (ok {:success true})
  )







