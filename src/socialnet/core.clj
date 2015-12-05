(ns socialnet.core
  (:require [com.stuartsierra.component :as component])
  (:require [socialnet.nrepl :as nrepl]
            [socialnet.state :as state]
            [socialnet.edn   :as dedn]
            [socialnet.util  :as util]))

(def system nil)

(defn make-system
  ([]
   (make-system :config))
  ([configuraton-designator]
   (let [config (dedn/resource-value configuraton-designator nil)]
     (component/system-map
      :config config
      :nrepl  (nrepl/make-nrepl-server config)))))



(defn init
  "Constructs the current development system."
  []
  (alter-var-root #'system (constantly (make-system))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system component/start))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system (fn [s]
                             (when s (component/stop s)))))

(defn go
  "Initializes and starts the current development system."
  []
  (init)
  (start))

;; TODO: do? (resolve conflicting dependencies )

;; (defn reset []
;;   (stop)
;;   (refresh :after 'user/go))

(defn -main []
  (go))
