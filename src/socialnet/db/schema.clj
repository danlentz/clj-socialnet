(ns socialnet.db.schema
  (:require [socialnet.edn :as dedn]
            [clojure.tools.logging :as log]
            [socialnet.util :as util]
            [socialnet.state :as state]
            [datomic.api :as d]))


(defn- containing [coll & items]
  (loop [coll coll items items]
    (if (empty? items)
      coll
      (recur
        (if (some  #(= % (first items)) coll)
          coll
          (conj coll (first items)))
        (rest items)))))

(defn schema-value [designator]
  (dedn/resource-value designator nil))

(defn register-schema! [& designators]
  (doseq [designator designators]
    (when (schema-value designator)
      (swap! state/state
        #(assoc %1
           :schema
           (containing (state/loaded-schema) designator)))))
  (state/loaded-schema))

(defn required-schema
  ([]
     (dedn/config-value :required-schema)))

(defn load-schema
  ([schema-designator]
     (load-schema (state/db-spec) schema-designator))
  ([db-spec schema-designator]
     (log/info :OP (str "Load " schema-designator) :DB db-spec)
     (d/create-database db-spec)
     (println (str "Loading " schema-designator " in " db-spec))
   (doseq [stmt (schema-value schema-designator)]
       (-> (d/connect db-spec)
         (d/transact stmt)
         (deref)))
     (register-schema! schema-designator)))

(defn- all-schema []
  (map schema-value (required-schema)))

(defn load-all-schema! [db-spec & schema-designators]
  (if (empty? schema-designators)
    (apply load-all-schema! db-spec (required-schema))
    (do
      (doseq [sd schema-designators]
        (load-schema db-spec sd))
      (state/loaded-schema))))

(defn ensure-schema [& schema-designators]
  (when (d/create-database (state/db-spec))
    (log/info :OP "Created Database" :DB (state/db-spec))
    (println (str "Created database " (state/db-spec)))
    (load-all-schema! (state/db-spec))))
