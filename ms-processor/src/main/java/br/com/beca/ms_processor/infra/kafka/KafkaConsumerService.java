package br.com.beca.ms_processor.infra.kafka;

import br.com.beca.ms_processor.application.dto.TransacaoEvento;
import br.com.beca.ms_processor.domain.entities.StatusTransacao;
import br.com.beca.ms_processor.domain.entities.Transacao;
import br.com.beca.ms_processor.infra.feign.CambioClient;
import br.com.beca.ms_processor.infra.feign.ContaMockDTO;
import br.com.beca.ms_processor.infra.feign.ContasClient;
import br.com.beca.ms_processor.infra.persistence.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class KafkaConsumerService {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private ContasClient contasClient;

    @Autowired
    private CambioClient cambioClient;

    @KafkaListener(topics = "transaction.requested", groupId = "transaction-processor-group")
    @Transactional
    public void consumir(TransacaoEvento evento) {
        System.out.println("DEBUG: Processando transação ID: " + evento.id());

        Transacao transacao = repository.findById(evento.id()).orElse(null);
        if (transacao == null) {
            System.err.println("Erro: Transação não encontrada.");
            return;
        }

        try {
            BigDecimal valorParaDebito = transacao.getValor();

            if ("USD".equalsIgnoreCase(transacao.getMoeda())) {
                var response = cambioClient.buscarCotacaoDolar();
                BigDecimal cotacao = new BigDecimal(response.moeda().cotacao());

                valorParaDebito = transacao.getValor().multiply(cotacao).setScale(2, RoundingMode.HALF_EVEN);

                System.out.println("Cotação USD: " + cotacao + " | Valor Convertido: R$ " + valorParaDebito);
            }

            ContaMockDTO conta = contasClient.buscarConta(evento.usuarioId());

            BigDecimal novoSaldo = conta.saldo().subtract(valorParaDebito);

            if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Saldo insuficiente. Atual: " + conta.saldo() + " | Necessário: " + valorParaDebito);
                transacao.atualizarStatus(StatusTransacao.REJECTED);
            } else {
                ContaMockDTO novaConta = new ContaMockDTO(conta.id(), novoSaldo, conta.limite());
                contasClient.atualizarSaldo(evento.usuarioId(), novaConta);

                transacao.atualizarStatus(StatusTransacao.APPROVED);
                System.out.println("Transação APROVADA!");
            }

        } catch (Exception e) {
            System.err.println("Erro técnico ao processar: " + e.getMessage());
            transacao.atualizarStatus(StatusTransacao.REJECTED);
        }

        repository.save(transacao);
    }
}