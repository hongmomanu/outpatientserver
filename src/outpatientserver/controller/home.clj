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

  (let [

         mydata (map #(conj {} {:name (str "jack" % (rand-int 10)) :value %}) (range 3))
         ]
    (println mydata)

    (map #(conj
            {:title (str "诊室" %)}
            {:data mydata})
      (range 8))
    )



      )

(defn getpatientbyid [patientid]

  [
   {:name "jack" :value 12}

   ]
  )

(defn getroomdatabyroomno [roomno]
      [
       {:name "jack" :value 12}
       {:name "jim" :value 12}
       {:name "smith" :value 12}

       ]

      )


(defn firebycall [roomno area patientid]
  (let [
         data (getpatientbyid patientid)
         roomdata (getroomdatabyroomno roomno)
         ]

       (doseq [channel (keys @websocket/channel-hub)]

              (when (= (get  (get @websocket/channel-hub channel) "content") area)

                    (send! channel (generate-string
                                     {
                                      :area area
                                      :data  (vec data)
                                      :type "callpatient"
                                      }
                                     )
                           false)


                    )

              (when (= (get  (get @websocket/channel-hub channel) "content") roomno)

                    (send! channel (generate-string
                                     {
                                      :area area
                                      :roomno roomno
                                      :data  (vec roomdata)
                                      :type "callpatient"
                                      }
                                     )
                           false)


                    )



              )

    )

  )


(defn makebigscreendataByArea [area]
      
      (println area "sssss")
      (let [
            bigdata (getdatabyarea area)
            ]
        ;(println bigdata)

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



