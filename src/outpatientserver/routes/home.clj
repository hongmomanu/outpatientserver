(ns outpatientserver.routes.home
  (:require [outpatientserver.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [outpatientserver.controller.home :as home]
            [outpatientserver.public.schedule :as schedule]
            [ring.util.http-response :refer [ok]]
            [pjstadig.utf8 :as putf8]
            [outpatientserver.public.funcs :as funcs]
            [ring.util.response :refer [file-response]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))


  (GET "/chinesetest" [name]
       (new String (.getBytes name "UTF-8") "UTF-8")

       )

  (GET "/stopschedule" [] (schedule/stop-schedule))

  (GET "/files/:filename" [filename]

         (file-response (str funcs/datapath "files/" filename))
       )

  (GET "/files/:filename1/:filename2" [filename1 filename2]

         (file-response (str funcs/datapath "files/" filename1 "/" filename2))

                )

  (GET "/duptest" [] (schedule/distinct-case-insensitive ["room" "123" "123" "234" "234"]))

  (GET "/sqltest" [area] (home/sqltest area))

  (GET "/firebycall" [roomno area hzxh hzxm zsmc status id] (home/firebycall roomno area hzxh hzxm zsmc status id))
  (POST "/firebycall" [roomno area hzxh hzxm zsmc status id] (home/firebycall roomno area hzxh hzxm zsmc status id))

  (GET "/firebydoctorlogin" [doctorid roomno] (home/firebydoctorlogin doctorid roomno))
  (POST "/firebydoctorlogin" [doctorid roomno] (home/firebydoctorlogin doctorid roomno))

  (GET "/firebychangeroom" [oldno newno newname] (home/firebychangeroom oldno newno newname))
  (POST "/firebychangeroom" [oldno newno newname] (home/firebychangeroom oldno newno newname))


  (GET "/firetipbyroom" [room content] (home/firetipbyroom room content))

  (GET "/firegetopenedroombyarea" [area] (home/firegetopenedroombyarea area))

  (POST "/firetipbyroom" [room content] (home/firetipbyroom room content))

  (GET "/firerefreshsystem" [room] (home/firerefreshsystem room))

  (GET "/updaterefreshtime" [times] (schedule/updaterefreshtime times))


  (GET "/clearscreen" [room] (home/clearscreen room))

    (GET "/getopenedroombyarea" [area] (home/getopenedroombyarea area))

  (GET "/fireprop" [room name value] (home/fireprop room name value))

  (GET "/fireplayvoice" [room content speed] (home/fireplayvoice room content speed))
  (POST "/fireplayvoice" [room content speed] (home/fireplayvoice room content speed))


           ;(GET "/setroomlines" [room lines] (home/clearscreen room))









  (GET "/about" [] (about-page)))

