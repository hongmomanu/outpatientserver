(ns outpatientserver.public.websocket
      (:use org.httpkit.server)
  (:require
            [clojure.data.json :as json]
            [taoensso.timbre :as timbre]
            )
)

(def channel-hub (atom {}))
(def bigscreen-hub-key (atom {}))


(defn handler [request]
  (with-channel request channel
    ;; Store the channel somewhere, and use it to sent response to client when interesting event happened
    ;;(swap! channel-hub assoc channel nil)
    (on-receive channel (fn [data]


                          (swap! channel-hub assoc channel (json/read-str data))
                          (println data (get (json/read-str data) "content"))

                               ;(println request)
                              ;(send! channel data)
                              ))
    (on-close channel (fn [status]

                        (println channel " disconnected. status: "  )
                        (swap! channel-hub dissoc channel)

                        ))))




(defn start-server [port]

  (run-server handler {:port port :max-body 16388608 :max-line 16388608})
  (timbre/info (str "outpatientserver-websocket started successfully on port" port))
  )


;(future (loop []
;          (println (keys @channel-hub))
;          (doseq [channel (keys @channel-hub)]
;            (println "ok")
;            (send! channel (json/write-str
;                                  {:happiness (rand 10)})
;              false)
;            )
;          (Thread/sleep 5000)
;          (recur)))







