package br.com.beca.ms_transacoes.infra.kafka;


import br.com.beca.ms_transacoes.application.dto.TransacaoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "transacao-criada";

    public void enviarEvento(TransacaoEvento evento) {
        try {
            kafkaTemplate.send(TOPIC, evento.id().toString(), evento);
            System.out.println("LOG KAFKA: Mensagem enviada -> " + evento);
        } catch (Exception e) {
            System.err.println("ERRO KAFKA: Falha ao enviar -> " + e.getMessage());
        }
    }
}