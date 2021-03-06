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
            [ring.util.http-response :refer [ok]]
            [taoensso.timbre :as timbre]
            [cheshire.core :refer :all]
            [clojure.data.json :as json]
            [outpatientserver.public.websocket :as websocket]
            [outpatientserver.public.funcs :as funcs]

            )
  )



;(declare )



(defn getdatabyarea [area]

  (let [

        areadata (group-by :zsmc (db/getbigscreendatabyarea area))
         ]

;(println areadata)

       (map #(conj
              {:title % :title2 (:ksmc (first (get areadata %))) :roomorder (:roomorder (first (get areadata %)))}
              {:data (get areadata %)}) (keys areadata))
    )



      )

(defn firegetopenedroombyarea [area]

  (timbre/info "fire getopenedroom by area  : " area )

  (doseq [channel (keys @websocket/channel-hub)]
    (when (= (get  (get @websocket/channel-hub channel) "content") area)

      (send! channel (generate-string
                       {
                         :area area
                         :data  (db/getopenedroombyarea area)
                         :type "getopenedroom"
                         }
                       )
        false)

      )
    )




  )
(defn getopenedroombyarea [area]
  (let [

         openrooms  (db/getopenedroombyarea area)
         ]



    (ok openrooms)
    )


  )

(defn sqltest [area]

      (ok (db/getbigscreendatabyarea area))
      )
(defn getpatientbyid [hzxh hzxm zsmc status id]

  [
   ;{:hzxm "jack" :hzxh (rand-int 100) :zsmc (rand-int 10)}
   {:hzxm hzxm :hzxh hzxh :zsmc zsmc :status status :id id}

   ]
  )

(defn getroomdatabyroomno [roomno]
      [
       {:name "jack" :value 12 :flag "1" }
       {:name "jim" :value 12 :flag "2" }
       {:name "smiths" :value 12 :flag "2"}

       ]

      )

(defn getdoctorinfobyid [doctorid]

      (let [
            imghost (:imghost (funcs/get-config-prop))
            ]
           (db/getdoctorinfobyid doctorid imghost)
           )



      #_[
       ;{:img "img/ionic.png" :info "华佗 ，字元化，一名旉，沛国谯县人，东汉末年著名的医学家。 华佗与董奉、张仲景并称为“建安三神医”。少时曾在外游学，行医足迹遍及安徽、河南、山东、江苏等地，钻研医术而不求仕途。他医术全面，尤其擅长外科，精于手术。并精通内、妇、儿、针灸各科。    晚年因遭曹操怀疑，下狱被拷问致死。 华佗被后人称为“外科圣手” 、“外科鼻祖”。被后人多用神医华佗称呼他，又以“华佗再世”、“元化重生”称誉有杰出医术的医师。"}
       {:img "img/doctor.jpg" :info "华佗 ，字元化，一名旉，沛国谯县人，东汉末年著名的医学家。 华佗与董奉、张仲景并称为“建安三神医”。少时曾在外游学，行医足迹遍及安徽、河南、山东、江苏等地，钻研医术而不求仕途。他医术全面，尤其擅长外科，精于手术。并精通内、妇、儿、针灸各科。    晚年因遭曹操怀疑，下狱被拷问致死。 华佗被后人称为“外科圣手” 、“外科鼻祖”。被后人多用神医华佗称呼他，又以“华佗再世”、“元化重生”称誉有杰出医术的医师。"}
       ]

      )

(defn firebychangeroom [oldno newno newname]

      (timbre/info "fire by change room  from  oldno : " oldno ",newno:" newno ",newname:" newname )
      (doseq [channel (keys @websocket/channel-hub)]
             (when (= (get  (get @websocket/channel-hub channel) "content") oldno)

                   (do (send! channel (generate-string
                                        {

                                         :roomno oldno
                                         :data  {:newno newno :newname newname}
                                         :type "changeroom"
                                         }
                                        )
                              false)
                       (swap! websocket/channel-hub assoc channel (conj (get @websocket/channel-hub channel) {:content newno}) )
                       )



                   )
             )

      )

(defn firerefreshsystem [room]

      (timbre/info "fire refresh system by  room : " room )

      (doseq [channel (keys @websocket/channel-hub)]
             (when (= (get  (get @websocket/channel-hub channel) "content") room)

                   (send! channel (generate-string
                                    {
                                     :room room
                                     :type "freshsystem"
                                     }
                                    )
                          false)

                   )
             )
      )

(defn clearscreen [room]

      (timbre/info "fire clear room : " room )
      (doseq [channel (keys @websocket/channel-hub)]
             (when (= (get  (get @websocket/channel-hub channel) "content") room)

                   (send! channel (generate-string
                                    {
                                     :room room
                                     :type "clearscreen"
                                     }
                                    )
                          false)

                   )
             )

      )
(defn fireprop [room name value]

      (timbre/info "fire prop room : " room ",name:" name ",value: " value)
      (doseq [channel (keys @websocket/channel-hub)]
             (when (= (get  (get @websocket/channel-hub channel) "content") room)

                   (send! channel (generate-string
                                    {
                                     :room room
                                     :name name
                                     :value value
                                     :type "fireprop"
                                     }
                                    )
                          false)

                   )
             )

)
(defn fireplayvoice [room content speed]
  (timbre/info "fire playvoice room : " room ",content:" content)
  (doseq [channel (keys @websocket/channel-hub)]
    (when (= (get  (get @websocket/channel-hub channel) "content") room)

      (send! channel (generate-string
                       {
                         :room room
                         :speed speed
                         :content content
                         :type "playvoice"
                         }
                       )
        false)

      )
    )

  )
(defn firebydoctorlogin [doctorid roomno]

      (let [
            data (getdoctorinfobyid doctorid)
            ]
           (timbre/info "firebydoctorlogin from doctorid:" doctorid ",roomno:" roomno)
           (doseq [channel (keys @websocket/channel-hub)]
              (when (= (get  (get @websocket/channel-hub channel) "content") roomno)

                 (send! channel (generate-string
                                  {
                                   :roomno roomno
                                   :data  (vec data)
                                   :type "doctorlogin"
                                   }
                                  )
                        false)

                 )
                  )


      )
      )

(defn firetipbyroom [room content]

      (timbre/info "fire tip  from  room : " room )

      (doseq [channel (keys @websocket/channel-hub)]

             (when    (= (get  (get @websocket/channel-hub channel) "content") room))

                    (send! channel (generate-string
                                     {
                                      :data  content
                                      :type "firetip"
                                      }
                                     )
                           false)
                    )



      )
(defn firebycall [roomno area hzxh hzxm zsmc status id]
  (let [
         data (getpatientbyid hzxh hzxm zsmc status id)
        ;roomdata (getroomdatabyroomno roomno)
         ]
       (timbre/info "fire by call from roomno : " roomno ",area:" area
                    ",hzxh:" hzxh ",hzxm:" hzxm ",zsmc:" zsmc ",status:"status ",id:"id)

    (future (doseq [channel (keys @websocket/channel-hub)]

              (when  (and (.contains  ["4"] status ) (= (get  (get @websocket/channel-hub channel) "content") area))

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
                                      :data  (vec data)
                                      :type "callpatient"
                                      }
                                     )
                           false)


                    )



              ))

    )

  )


(defn makebigscreendataByArea [area]
      

      (timbre/info "get data from area : " area )
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



