(ns itbot.config
  (:require
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [java-time.api :as jt]
   [malli.core :as m]
   [malli.error :as me]
   [mount.core :refer [defstate args]]
   [mlib.envvar :refer [env-str env-int]]
   ,))


(defn build-info []
  (-> "build-info.edn" (io/resource) (slurp) (edn/read-string)))


(defn env-config []
  {:telegram-apikey   (env-str "TELEGRAM_APIKEY")
   ; :database-url      (env-str "DATABASE_URL")
   ;
   :timezone (jt/zone-id (env-str "TIMEZONE" "Asia/Irkutsk"))

   ; :redis-url           (env-str "REDIS_URL")                 ;; "redis://user:password@localhost:6379/"
   ;
   :build-info (build-info)})


(def not-blank
  (m/schema [:re #"[^\s]+"]))

(def pos-int
  (m/schema [:and :int [:> 0]]))


(def config-schema
  [:map 
   [:telegram-apikey not-blank]
;   [:database-url    not-blank]
   ])


(defn validate-config [cfg]
  (if (m/validate config-schema cfg)
    cfg
    (->> cfg
         (m/explain config-schema)
         (me/humanize)
         (ex-info "invalid-config")
         (throw))))


(defn make-config []
  (validate-config (env-config)))


(defstate config 
  :start (args))

