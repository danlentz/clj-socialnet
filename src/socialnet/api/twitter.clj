(ns socialnet.api.twitter
  (:require [socialnet.util :as util]
            [socialnet.edn  :as dedn])
  (:require [twitter.oauth :as oauth :refer :all]
            [twitter.callbacks :as cb :refer :all]
            [twitter.callbacks.handlers :as handler :refer :all]
            [twitter.api.restful :as api :refer :all])
  (:import  [twitter.callbacks.protocols SyncSingleCallback]))



(def my-creds nil #_(make-oauth-creds *app-consumer-key*
                                *app-consumer-secret*
                                *user-access-token*
                                *user-access-token-secret*))


#_
(users-show :oauth-creds my-creds :params {:screen-name "bobdc"})
#_
(followers-list :oauth-creds my-creds
                :params {:target-screen-name "danlentz"})


(defn fetch-user [s]
  (users-show :oauth-creds my-creds
              :params {:screen-name s}))

(defn fetch-user-by-id [id]
  (users-lookup :oauth-creds my-creds
                :params {:user-id id}))

(defn fetch-follower-ids [s]
  (:ids (followers-ids    :oauth-creds my-creds
                          :callbacks (SyncSingleCallback. response-return-body
                                                          response-throw-error
                                                          exception-rethrow)
                          :params {:target-screen-name s})))




; shows the users friends

; use a custom callback function that only returns the body of the response
(friendships-show :oauth-creds my-creds
                  :callbacks (SyncSingleCallback. response-return-body
                                                  response-throw-error
                                                  exception-rethrow)
          :params {:target-screen-name "danlentz"})

; post a text status, using the default sync-single callback
(statuses-update :oauth-creds *creds*
                 :params {:status "hello world"})

; upload a picture tweet with a text status attached, using the default sync-single callback
(statuses-update-with-media :oauth-creds *creds*
                            :body [(file-body-part "/pics/test.jpg")
                                  (status-body-part "testing")])
