(ns socialnet.db.datatype
  "Datatype Metamodel API"
  (:require [datomic.api        :as    d]
            [datomic.db         :as  ddb]
            [datomic.common     :as  dcm]
            [clojure.pprint     :as   pp]
            [socialnet.db.rules :refer [defrule clear-rulebase! all-rules] :as rule]
            [socialnet.db.fn    :refer [defdbfn dbfn clear-fnbase! all-dbfn] :as fn]
            [socialnet.db.ops   :as   db]))

(defn entity [e]
  (d/entity (db/db) e))

(defn describe
  "Returns the Concise Bounded Description (CBD) of an entity 'e'"
  [e]
  (d/touch (entity e)))

(defn all-datatypes []
  (map first
    (d/q '[:find ?dt :in $ :where
           [?e :dt/dt :dt/dt]
           [?e :db/ident ?dt]]
      (db/db))))

(defn datatype-doc [dt]
  (:db/doc (entity dt)))

(defn datatype-parents [dt]
  (:dt/parent (entity dt)))

(defn datatype-ancestors [dt]
  (let [direct-parents (datatype-parents dt)]
    (distinct
      (concat direct-parents
        (mapcat datatype-parents direct-parents)))))

(defrule direct-slot [?dt ?s]
  [?dt :dt/slots ?i]
  [?i  :db/ident ?s])

(defn datatype-direct-slots [dt]
  (:dt/slots (entity dt)))

;; (defn datatype-slots [dt]
;;   (into (reduce clojure.set/union
;;           (map datatype-direct-slots (datatype-ancestors dt)))
;;     (datatype-direct-slots dt)))

(defrule effective-slot [?dt ?s]
  [?dt :dt/slots ?i]
  [?i  :db/ident ?s])

(defrule effective-slot [?dt ?s]
  [?dt  :dt/parent ?p]
  (effective-slot ?p ?s))

(defn datatype-slots [dt]
  (set
    (map first
      (d/q '[:find ?s :in $ % ?dt :where
             (effective-slot ?dt ?s)]
        (db/db) (all-rules) dt))))


(defn slot-valuetype [dt slot]
  (ffirst
    (d/q '[:find ?t :in $ ?dt ?s :where
           [?e :dt/dt    :dt/dt]
           [?e :db/ident    ?dt]
           [?i :db/ident     ?s]
           [?e :dt/slots     ?i]
           [?i :db/valueType ?v]
           [?v :db/ident     ?t]]
      (db/db) dt slot)))

(defn slot-doc [dt slot]
  (ffirst
    (d/q '[:find ?d :in $ ?dt ?s :where
           [?e :dt/dt    :dt/dt]
           [?e :db/ident    ?dt]
           [?i :db/ident     ?s]
           [?e :dt/slots     ?i]
           [?i :db/doc      ?d]]
      (db/db) dt slot)))

(defn slot-cardinality [dt slot]
  (ffirst
    (d/q '[:find ?c :in $ ?dt ?s :where
           [?e :dt/dt      :dt/dt]
           [?e :db/ident      ?dt]
           [?i :db/ident       ?s]
           [?e :dt/slots       ?i]
           [?i :db/cardinality ?v]
           [?v :db/ident       ?c]]
      (db/db) dt slot)))

(defn slot-uniqueness [dt slot]
  (ffirst
    (d/q '[:find ?u :in $ ?dt ?s :where
           [?e :dt/dt      :dt/dt]
           [?e :db/ident      ?dt]
           [?i :db/ident       ?s]
           [?e :dt/slots       ?i]
           [?i :db/unique      ?v]
           [?v :db/ident       ?u]]
      (db/db) dt slot)))

;; TODO: change of semantics from metaclass to class?

(defn map-datatype-slots [f dt]
  (map (partial f dt) (datatype-slots dt)))

(defn slotwise [f dt]
  (let [slots (datatype-slots dt)
        vals  (map-datatype-slots f dt)]
  (zipmap slots vals)))

(defn slot-valuetypes [dt]
  (slotwise slot-valuetype dt))

(defn slot-docs [dt]
  (slotwise slot-doc dt))

(defn slot-cardinalities [dt]
  (slotwise slot-cardinality dt))

(defn slot-uniquenesses [dt]
  (slotwise slot-uniqueness dt))

(defn about [dt]
  ;; TODO: do
  )




(defn entity-datatype [e]
  (:dt/dt (entity e)))

(defn entity-slots [e]
  (datatype-slots (entity-datatype e)))


;; (db/db)
;; (all-datatypes)
;; (describe :dt/dt)
;; (datatype :dt/dt)
;; (datatype-slots :any)
