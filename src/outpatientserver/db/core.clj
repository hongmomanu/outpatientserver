(ns outpatientserver.db.core
  (:use korma.core
        [korma.db :only [defdb with-db]])
  (:require
    [clojure.java.jdbc :as jdbc]
    ;[yesql.core :refer [defqueries]]
    [taoensso.timbre :as timbre]
    [clojure.java.io :as io]
    [environ.core :refer [env]])
  (:import java.sql.BatchUpdateException))

(def datapath (str (.getName (io/file ".")) "/"))

(defn get-config-prop []
      (let [filename (str datapath "server.config")]
           (read-string (slurp filename))
           )
      )

(def db-sqlserver {:classname "net.sourceforge.jtds.jdbc.Driver"
                   :subprotocol "jtds:sqlserver"
                   :subname (let [prop (get-config-prop)
                                  sqlserver (:sqlserver prop)
                                  ]
                                 (str (:address sqlserver) ";user="
                                      (:user sqlserver ) ";password=" (:pass sqlserver)
                                      ))
                   })


#_(defn getbigscreendatabyarea [area]

      (with-db db-sqlserver
               (exec-raw ["select mz_fz_records.dept_name as ksmc ,
               mz_fz_records.charge_status,case mz_fz_records.charge_type
               when '3' then '专家' + mz_fz_rooms.room_name when '4' then
               '专家' +mz_fz_rooms.room_name when '7' then '专家' +mz_fz_rooms.room_name  else ' '
               +mz_fz_rooms.room_name end as zsmc,mz_fz_records.patient_name as hzxm,case
                op_type when '2' then '回诊 ' else cast (visit_order as varchar) end as hzxh,ampm
                from mz_fz_records,mz_fz_rooms   where (mz_fz_records.visit_dept=mz_fz_rooms.dept_code)
                 and   (mz_fz_records.room_id=mz_fz_rooms.room_id) and (mz_fz_records.area_code=?) and
                  mz_fz_records.charge_status  in ('3','4')
                  order by mz_fz_records.charge_status desc,mz_fz_records.dept_name asc,
                  mz_fz_records.wait_order asc" [area]] :results)
               )

      )

(defn  getbigscreendatabyarea [area]
        (with-db db-sqlserver
                 (exec-raw
                   ["SELECT mz_fz_records.dept_name as  ksmc,
                      case mz_fz_records.charge_type when '3' then '专家 '+tbl_mzpd_room.room_name
                      when '4' then '专家 '+tbl_mzpd_room.room_name when '7'
                      then '专家 '+tbl_mzpd_room.room_name else '  '+tbl_mzpd_room.room_name end as zsmc,
                       mz_fz_records.patient_name as hzxm,
                         case op_type when '2' then '回诊' else cast(visit_order as varchar) end as hzxh ,ampm   ,
                           mz_fz_records.mz_xh     FROM tbl_mzpd_room,      mz_fz_records,  mz_fz_rooms
                            WHERE ( mz_fz_records.area_code = tbl_mzpd_room.room_area ) and
                                   ( mz_fz_records.room_id = tbl_mzpd_room.room_id ) and
                                            ( mz_fz_records.visit_dept = mz_fz_rooms.dept_code ) and
                                              ( mz_fz_records.room_id = mz_fz_rooms.room_id ) and
                                              ( mz_fz_records.area_code = ? ) AND
                                               ( mz_fz_records.charge_status in ('3','4'))
                                               ORDER BY tbl_mzpd_room.room_order ASC, mz_fz_records.charge_status DESC,
                                                mz_fz_records.wait_order ASC " [area]] :results

                   )


                 )


        )

(defn getdoctorinfobyid [code imghost]
      (with-db db-sqlserver

               (exec-raw [(str "select name,title,('" imghost "'+pic) as img,subjects,cast(cv as nvarchar(1000)) as cv,case sex when '1' then '男' else '女' end as sex from a_employee_introduction where a_employee_introduction.code=?")   [code]] :results)
               )

      )
#_(def conn
  {:classname   "org.h2.Driver"
   :connection-uri (:database-url env)
   :make-pool?     true
   :naming         {:keys   clojure.string/lower-case
                    :fields clojure.string/upper-case}})

#_(defqueries "sql/queries.sql" {:connection conn})
