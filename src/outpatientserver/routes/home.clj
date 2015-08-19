(ns outpatientserver.routes.home
  (:require [outpatientserver.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [outpatientserver.controller.home :as home]
            [outpatientserver.public.schedule :as schedule]
            [ring.util.http-response :refer [ok]]
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

  (GET "/stopschedule" [] (schedule/stop-schedule))

  (GET "/files/:filename" [filename]

         (file-response (str funcs/datapath "files/" filename))
       )

  (GET "/files/:filename1/:filename2" [filename1 filename2]

         (file-response (str funcs/datapath "files/" filename1 "/" filename2))

                )

  (GET "/duptest" [] (schedule/distinct-case-insensitive ["room" "123" "123" "234" "234"]))

  (GET "/sqltest" [area] (home/sqltest area))

  (GET "/firebycall" [roomno area hzxh hzxm status] (home/firebycall roomno area hzxh hzxm status))

  (GET "/firebydoctorlogin" [doctorid roomno] (home/firebydoctorlogin doctorid roomno))

  (GET "/firebychangeroom" [oldno newno newname] (home/firebychangeroom oldno newno newname))

  (GET "/updaterefreshtime" [times] (schedule/updaterefreshtime times))






  (GET "/about" [] (about-page)))

