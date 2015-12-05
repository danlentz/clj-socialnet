(ns socialnet.user
  "User REPL environment for db interaction"
  (:require [datomic.api           :as d])
  (:require [datomic.db            :as ddb])
  (:require [datomic.common        :as dcm])
  (:require [socialnet.util        :as util])
  (:require [socialnet.db.ops      :as db])
  (:require [socialnet.db.rules    :refer [defrule clear-rulebase! all-rules] :as rules])
  (:require [socialnet.db.fn       :refer [dbfn defdbfn clear-fnbase! all-dbfn] :as fn])
  (:require [socialnet.db.datatype :as dt])
  (:require [socialnet.state       :as state])
  (:require [socialnet.core        :as core])
  (:require [socialnet.edn         :as dedn])
  (:require [socialnet.model.twit  :as twit])
  (:require [clojure.edn           :as edn])
  (:require [clojure.pprint        :as pp])
  (:require [print.foo             :refer :all :as foo]))


(defn foo []
  nil)
