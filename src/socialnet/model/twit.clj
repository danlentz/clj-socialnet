(ns socialnet.model.twit
  (:refer-clojure :exclude [cat])
  (:require [datomic.api        :as    d]
            [datomic.db         :as  ddb]
            [datomic.common     :as  dcm]
            [clojure.pprint     :as   pp]
            [socialnet.db.rules :refer [defrule clear-rulebase! all-rules] :as rule]
            [socialnet.db.fn    :refer [defdbfn dbfn clear-fnbase! all-dbfn] :as fn]
            [socialnet.db.ops   :refer [entity describe] :as db]
            [socialnet.util :as util]
            [socialnet.api.twitter :as twitter]
            [clojure.tools.logging :as log]))
