(ns outpatientserver.controller.home

  (:import
           (java.text SimpleDateFormat)
           (java.util  Date Calendar)
           )
  (:use compojure.core org.httpkit.server)
  (:use ruiyun.tools.timer)
  (:use [taoensso.timbre :only [trace debug info warn error fatal]])
  (:require [outpatientserver.db.core :as db]
            [outpatientserver.layout :as layout]
            [cheshire.core :refer :all]
            [clojure.data.json :as json]
            [outpatientserver.public.websocket :as websocket]
            [outpatientserver.public.funcs :as funcs]

            )
  )



;(declare )



(defn getdatabyarea [area]

      [{:title "诊室1" :data [
                            {:name "jack" :value "sss"}
                            {:name "jack" :value "sss"}
                            {:name "jack" :value "sss"}
                            ]
        }
       {:title "诊室2" :data [
                            {:name "jackm" :value "sss"}
                            {:name "jacks" :value "sss"}
                            {:name "jacks" :value "sss"}
                            ]
        }
       {:title "诊室3" :data [
                            {:name "jack" :value "sss"}
                            {:name "jack" :value "sss"}
                            {:name "jack" :value "sss"}
                            ]
        }

       ]
      )

(defn makebigscreendataByArea [area]
      
      (println area)
      (let [
            bigdata (getdatabyarea area)
            ]

           (doseq [channel (keys @websocket/channel-hub)]

                  (when (= (get  (get @websocket/channel-hub channel) "content") area)

                        (send! channel (generate-string
                                         {
                                          :area area
                                          :data  (vec bigdata)
                                          :type "bigscreendata"
                                          }
                                         )
                               false)


                        )



                  )

           )



      )

(defn scheduleFunc []
      (let [
            alldata  (vals @websocket/channel-hub)
            filterdata (filter (fn [x]
                        (= (get  x "type") "mainscreen"))
                               alldata
                    )
            filterrooms (map #(get % "content" ) filterdata)
            rooms (funcs/distinct-case-insensitive filterrooms)
            ]


           (future (doseq [room rooms]
                          (makebigscreendataByArea room)
                          ))


           )
      )



