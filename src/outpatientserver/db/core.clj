(ns outpatientserver.db.core
  (:use korma.core
        [korma.db :only [defdb with-db]])
  (:require
    [clojure.java.jdbc :as jdbc]
    ;[yesql.core :refer [defqueries]]
    [taoensso.timbre :as timbre]
    [environ.core :refer [env]])
  (:import java.sql.BatchUpdateException))

#_(def conn
  {:classname   "org.h2.Driver"
   :connection-uri (:database-url env)
   :make-pool?     true
   :naming         {:keys   clojure.string/lower-case
                    :fields clojure.string/upper-case}})

#_(defqueries "sql/queries.sql" {:connection conn})
