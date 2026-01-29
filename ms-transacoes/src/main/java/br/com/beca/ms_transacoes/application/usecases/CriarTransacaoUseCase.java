package br.com.beca.ms_transacoes.application.usecases;

import br.com.beca.ms_transacoes.application.dto.TransacaoEvento;
import br.com.beca.ms_transacoes.domain.entities.Transacao;
import br.com.beca.ms_transacoes.infra.feign.CambioClient;
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

    @Autowired
    private CambioClient cambioClient;

    @Transactional
    public void executar(Long usuarioId, BigDecimal valor, String moeda) {
        String cotacaoAtual = "0.00";
        try {
            var resposta = cambioClient.buscarCotacaoDolar();
            cotacaoAtual = resposta.moeda().cotacao();
            System.out.println("Cotação Dólar Hoje: " + cotacaoAtual);
        } catch (Exception e) {
            System.out.println("Erro ao buscar cotação: " + e.getMessage());
        }
        System.out.println("DEBUG: 1. Iniciando UseCase...");

        Transacao transacao = new Transacao(usuarioId, valor, moeda);
        repository.save(transacao);
        System.out.println("DEBUG: 2. Salvo no Banco com Sucesso!");

        TransacaoEvento evento = TransacaoEvento.fromEntity(transacao);
        System.out.println("DEBUG: 3. Tentando enviar para o Kafka...");

        kafkaService.enviarEvento(evento);

        System.out.println("DEBUG: 4. Enviado (ou enfileirado) para o Kafka!");
    }
}