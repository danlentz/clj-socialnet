(ns socialnet.model.twit
  (:refer-clojure :exclude [cat])
  (:require [datomic.api        :as    d]
            [datomic.db         :as  ddb]
            [datomic.common     :as  dcm]
            [clojure.pprint     :as   pp]
            [socialnet.util     :as util]
            [socialnet.state    :as state]
            [socialnet.db.datatype :as dt]
            [socialnet.db.rules :refer [defrule clear-rulebase! all-rules] :as rule]
            [socialnet.db.fn    :refer [defdbfn dbfn clear-fnbase! all-dbfn] :as fn]
            [socialnet.db.ops   :refer [entity describe] :as db]
            [socialnet.api.twitter :as twitter]
            [clojure.tools.logging :as log]))

(def me  7623072)

(defn ensure-twits [& more]
  (vals (:tempids @(d/transact (db/conn)
                               (for [ident more]
                                 {:db/id #db/id[:db.part/user]
                                  :dt/dt :twit
                                  :twit/id id})))))

(defn ensure-twit-id [n]
  (first (ensure-twit-ids n)))

(defn upsert-twit! [{id :id
                     name :screen_name
                     namestring :name
                     lang :lang
                     description :description

                     }]
