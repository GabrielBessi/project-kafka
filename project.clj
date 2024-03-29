(defproject project-kafka "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.fzakaria/slf4j-timbre "0.3.17"]
                 [org.apache.kafka/kafka-streams "3.2.1"]
                 [org.clojure/data.json "2.4.0"]
                 [confluent-kafka-clojure "1.8.0"]
                 [cassj "0.3.3"]]
  :main ^:skip-aot project-kafka.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
