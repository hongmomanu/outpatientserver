(ns outpatientserver.public.schedule
      (:use org.httpkit.server)
      (:use ruiyun.tools.timer)
  (:require
            [clojure.data.json :as json]
            [taoensso.timbre :as timbre]
            [outpatientserver.controller.home :as home]
            [outpatientserver.public.funcs :as funcs]
            [ring.util.http-response :refer [ok]]
            )
)

(def schedule-timer (atom {}))

(defn distinct-case-insensitive [xs]
  (->> xs
    (group-by clojure.string/lower-case)
    (vals)
    (map first)))

(defn start-schedule []

  (timbre/info "timer  schedule  started")
  (swap! schedule-timer assoc "time" (timer "The timer for schedule"))


  (run-task! #(home/scheduleFunc ) :period (:refreshtime (funcs/get-config-prop))   :by (get @schedule-timer "time") )

  )
(defn stop-schedule[]
  (timbre/info "timer  schedule  stoped")

      (cancel! (get @schedule-timer "time"))

  )

(defn updaterefreshtime [times]

      (timbre/info "update refresh time :" times)
      (let [
            content (funcs/get-config-prop)
            newcontent (conj content {:refreshtime (read-string times) })
            ]

           (timbre/info "update refresh time:" times)
           (funcs/update-config-prop (str newcontent))
           (stop-schedule)
           (start-schedule)
           (ok {:success true})
           )

      )








