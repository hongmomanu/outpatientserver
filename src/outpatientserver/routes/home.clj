(ns outpatientserver.routes.home
  (:require [outpatientserver.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [outpatientserver.controller.home :as home]
            [outpatientserver.public.schedule :as schedule]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))

  (GET "/stopschedule" [] (schedule/stop-schedule))

  (GET "/duptest" [] (schedule/distinct-case-insensitive ["room" "123" "123" "234" "234"]))

  (GET "/about" [] (about-page)))

