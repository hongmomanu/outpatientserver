(ns outpatientserver.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [outpatientserver.layout :refer [error-page]]
            [outpatientserver.routes.home :refer [home-routes]]
            [outpatientserver.public.websocket :as websocket]
            [outpatientserver.public.schedule :as schedule]
            [outpatientserver.middleware :as middleware]
            [outpatientserver.db.core :as db]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     (if (env :dev) :trace :info)
     :appenders {:rotor (rotor/rotor-appender
                          {:path "outpatientserver.log"
                           :max-size (* 512 1024)
                           :backlog 10})}})

  (if (env :dev) (parser/cache-off!))
  (timbre/info (str
                 "\n-=[outpatientserver started successfully"
                 (when (env :dev) " using the development profile")
                 "]=-"))
  (websocket/start-server 3001)
  (schedule/start-schedule)
  ;(timbre/info "outpatientserver-websocket started successfully")
  )

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "outpatientserver is shutting down...")
  (timbre/info "shutdown complete!"))

(def app-routes
  (routes
    home-routes
    #_(wrap-routes #'home-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
