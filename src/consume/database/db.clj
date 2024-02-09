(ns consume.database.db 
  (:require [clojure.data.json :as json]
            [cassj.core :as cassandra]))

(defn processar-mensagem [mensagem]
  (let [dados (json/read-str (:value mensagem) :key-fn keyword)
        id (get dados :id)
        status (get dados :status)
        velocidade (get dados :velocidade)
        placa (get dados :placa)
        multa (get dados :multa)
        pontos (get dados :pontos)]
    (cassandra/execute "INSERT INTO veiculos_multas (id, status, velocidade, placa, multa, pontos)
                        VALUES (?, ?, ?, ?, ?, ?)" id status velocidade placa multa pontos)))
