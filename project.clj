(defproject clj-socialnet "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :resource-paths ["schema" "resources"]

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
		 [twitter-api "1.0.0"]
                 [clj-http "2.0.0"]
                 [cheshire "5.5.0"]
                 [com.datomic/datomic-pro "0.9.5153" :exclusions [joda-time]]
                 [com.stuartsierra/component "0.3.0"]
                 [datomic-schematode "0.1.0-RC3"]
                 [print-foo "1.0.2"]]

  :plugins [[lein-asciidoctor  "0.1.14"]
            [cider/cider-nrepl "0.9.1"]]

  :asciidoc {:sources ["doc/*.adoc"]
             :to-dir "doc/html"
             :toc              :left
             :doctype          :article
             :format           :html5
             :extract-css      true
             :source-highlight true})
