(ns socialnet.api.twitter
  (:require [socialnet.util :as util]
            [socialnet.edn  :as dedn])
  (:require [twitter.oauth :as oauth :refer :all]
            [twitter.callbacks :as cb :refer :all]
            [twitter.callbacks.handlers :as handler :refer :all]
            [twitter.api.restful :as api :refer :all])
  (:import  [twitter.callbacks.protocols SyncSingleCallback]))



(defn- make-creds []
  ((juxt :consumer-key :consumer-secret :access-token :access-secret)
   (dedn/config-value :auth :twitter)))

(defn creds []
  (apply make-oauth-creds (make-creds)))


(defn fetch-user [screen-name]
  (:body (users-show :oauth-creds (creds)
                     :params {:screen-name screen-name})))

(defn fetch-user-by-id [id]
  (:body (users-lookup :oauth-creds (creds)
                       :params {:user-id id})))

(defn fetch-follower-ids [screen-name]
  (:ids (followers-ids    :oauth-creds (creds)
                          :callbacks (SyncSingleCallback. response-return-body
                                                          response-throw-error
                                                          exception-rethrow)
                          :params {:target-screen-name screen-name})))

(comment






(fetch-user "danlentz")





#_
(users-show :oauth-creds my-creds :params {:screen-name "bobdc"})
#_
(followers-list :oauth-creds my-creds
                :params {:target-screen-name "danlentz"})

; shows the users friends

; use a custom callback function that only returns the body of the response
(friendships-show :oauth-creds (creds)
                  :callbacks (SyncSingleCallback. response-return-body
                                                  response-throw-error
                                                  exception-rethrow)
          :params {:target-screen-name "bobdc"})

; post a text status, using the default sync-single callback
(statuses-update :oauth-creds *creds*
                 :params {:status "hello world"})

; upload a picture tweet with a text status attached, using the default sync-single callback
(statuses-update-with-media :oauth-creds *creds*
                            :body [(file-body-part "/pics/test.jpg")
                                  (status-body-part "testing")])
)
