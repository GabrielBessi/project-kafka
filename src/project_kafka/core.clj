(ns project-kafka.core
  (:import [java.util Properties]
           [org.apache.kafka.common.serialization Serdes Serde Serializer Deserializer StringSerializer StringDeserializer]
           [org.apache.kafka.streams KafkaStreams StreamsConfig Topology]
           [org.apache.kafka.streams.processor Processor ProcessorSupplier To])
  (:require [taoensso.timbre :as log]
            [clojure.data.json :as json])
  (:gen-class))

(deftype Deserializador []
 Deserializer
  (close [_])
  (configure [_ configs isKey])
  (deserialize [_ topic data]
    (json/read-str (.deserialize (StringDeserializer.) topic data) :key-fn keyword)))

(deftype Serializador []
 Serializer 
  (close[_])
  (configure [_ configs isKey])
  (serialize [_ topic data] (StringSerializer.) topic (json/write-str data)))

(deftype Serde-personalizado []
  Serde
  (close[_])
  (configure [_ configs isKey])
  (deserializer [_] (Deserializador.))
  (serializer [_] (Serializador.)))

(def props 
  (doto (Properties.)
    (.putAll
     {StreamsConfig/APPLICATION_ID_CONFIG            "trt-topology"
      StreamsConfig/BOOTSTRAP_SERVERS_CONFIG         "host.docker.internal:9096"
      StreamsConfig/DEFAULT_KEY_SERDE_CLASS_CONFIG   (.getClass (Serdes/String))
      StreamsConfig/DEFAULT_VALUE_SERDE_CLASS_CONFIG (.getClass (Serde-personalizado.))})))

(deftype Processador [^:volatile-mutable context]
  Processor
  (close [_])
  (init [_ c]
    (set! context c))
  (process [_ k msg]))

(deftype Suplier-processador []
  ProcessorSupplier
  (get [_]
    (Processador. nil)))

(defn topology []
  (doto (Topology.)
    (.addSource    ""                        (into-array String [""]))
    (.AddProcessor "" (Suplier-processador.) (into-array String [""]))
    (.addSink      "" ""                     (into-array String [""]))))

(defn -main [& args]
  (.start (KafkaStreams. (topology) props)))
