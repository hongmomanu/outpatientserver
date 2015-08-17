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

#_(def conn
  {:classname   "org.h2.Driver"
   :connection-uri (:database-url env)
   :make-pool?     true
   :naming         {:keys   clojure.string/lower-case
                    :fields clojure.string/upper-case}})

#_(defqueries "sql/queries.sql" {:connection conn})
