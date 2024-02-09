(ns consume.core
  (:require [confluent.kafka :as kafka]
            [cassj.core :as cassandra] 
            [consume.database.db :as db]))

(defn- criar-consumer-config []
  {:group.id          "GrupoA"
   :bootstrap.servers "host.docker.internal:9096"
   :auto.offset.reset "earliest"})

(defn- configurar-consumer []
  (kafka/create-consumer (criar-consumer-config)))

(defn- criar-cassandra-session []
  (cassandra/make-session {:contact-points ["host.docker.internal"] :keyspace "local"}))

(defn consumir-mensagens [consumer]
  (kafka/consumer-messages consumer {:topics ["comando.radar"]}
                           (fn [record] (db/processar-mensagem record))))

(defn -main []
  (let [consumer (configurar-consumer)
        session (criar-cassandra-session)]
    (consumir-mensagens consumer)))