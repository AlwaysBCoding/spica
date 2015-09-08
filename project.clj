(defproject alwaysbcoding/spica "0.0.2-SNAPSHOT"
  :description "A framework for building web applications with Clojure and Datomic"
  :url "http://swish.i://github.com/AlwaysBCoding/spica"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.datomic/datomic-pro "0.9.5130" :exclusions [joda-time]]
                 [camel-snake-kebab "0.3.2"]]

  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :username "Jordan@mainstreetgenome.com"
                                   :password "36494eb3-fa9d-4e90-8106-f32541732be8"}})
