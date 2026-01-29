package br.com.beca.ms_transacoes.application.usecases;

import br.com.beca.ms_transacoes.application.dto.TransacaoEvento;
import br.com.beca.ms_transacoes.domain.entities.Transacao;
import br.com.beca.ms_transacoes.infra.kafka.KafkaProducerService;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class CriarTransacaoUseCase {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private KafkaProducerService kafkaService;


    @Transactional
    public void executar(Long usuarioId, BigDecimal valor, String moeda) {
        System.out.println("DEBUG: 1. Iniciando UseCase (Command API)...");

        Transacao transacao = new Transacao(usuarioId, valor, moeda);

        repository.save(transacao);
        System.out.println("DEBUG: 2. Transação salva ID: " + transacao.getId() + " - Status: PENDING");

        TransacaoEvento evento = TransacaoEvento.fromEntity(transacao);
        kafkaService.enviarEvento(evento);

        System.out.println("DEBUG: 3. Evento enfileirado no Kafka com sucesso!");
    }
}