(ns outpatientserver.public.websocket
      (:use org.httpkit.server)
    (:import
      (java.util  Date )
      (java.text SimpleDateFormat)
      )
  (:require
            [clojure.data.json :as json]
            [taoensso.timbre :as timbre]
            [cheshire.core :refer :all]
            )
)

(def channel-hub (atom {}))


(defn handler [request]
  (with-channel request channel
    ;; Store the channel somewhere, and use it to sent response to client when interesting event happened
    ;;(swap! channel-hub assoc channel nil)
    (on-receive channel (fn [data]


                          (swap! channel-hub assoc channel (json/read-str data))
                            (timbre/info data)

                            (let [
                                  df   (new SimpleDateFormat "yyyy-MM-dd HH:mm:ss")
                                  today (new Date)
                                  todaystr (.format df (.getTime today))

                                  ]
                                 (send! channel (generate-string
                                                  {
                                                   :time  todaystr
                                                   :type "servertime"
                                                   }
                                                  )
                                        false)
                                 )



                               ;(println request)
                              ;(send! channel data)
                              ))
    (on-close channel (fn [status]

                        (timbre/info channel " disconnected. status: "  )
                        (swap! channel-hub dissoc channel)

                        ))))




(defn start-server [port]

  (run-server handler {:port port :max-body 16388608 :max-line 16388608})
  (timbre/info (str "outpatientserver-websocket started successfully on port" port))
  )








