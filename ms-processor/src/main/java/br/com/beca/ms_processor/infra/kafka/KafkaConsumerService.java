package br.com.beca.ms_processor.infra.kafka;

import br.com.beca.ms_processor.application.dto.TransacaoEvento;
import br.com.beca.ms_processor.domain.entities.StatusTransacao;
import br.com.beca.ms_processor.domain.entities.Transacao;
import br.com.beca.ms_processor.infra.feign.CambioClient;
import br.com.beca.ms_processor.infra.feign.ContaMockDTO;
import br.com.beca.ms_processor.infra.feign.ContasClient;
import br.com.beca.ms_processor.infra.persistence.TransacaoRepository;
import br.com.beca.ms_processor.infra.persistence.entities.TransacaoEntity;
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

    @KafkaListener(topics = "transacao-criada", groupId = "transaction-processor-group")
    @Transactional
    public void consumir(TransacaoEvento evento) {
        System.out.println("DEBUG: Processando transação ID: " + evento.id());

        TransacaoEntity entity = repository.findById(evento.id()).orElse(null);

        if (entity == null) {
            System.err.println("Erro: Transação não encontrada no banco.");
            return;
        }

        Transacao transacaoDomain = entity.toDomain();

        try {
            BigDecimal valorParaDebito = transacaoDomain.getValor();

            if ("USD".equalsIgnoreCase(transacaoDomain.getMoeda())) {
                var cambio = cambioClient.buscarCotacao("USD");
                BigDecimal cotacao = BigDecimal.valueOf(cambio.bid());

                valorParaDebito = transacaoDomain.getValor().multiply(cotacao).setScale(2, RoundingMode.HALF_EVEN);

                System.out.println("Cotação USD: " + cotacao + " | Valor Convertido: R$ " + valorParaDebito);
            }

            ContaMockDTO conta = contasClient.buscarConta(evento.usuarioId());
            BigDecimal novoSaldo = conta.saldo().subtract(valorParaDebito);

            if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Saldo insuficiente. Atual: " + conta.saldo() + " | Necessário: " + valorParaDebito);
                transacaoDomain.atualizarStatus(StatusTransacao.REJECTED);
            } else {
                ContaMockDTO novaConta = new ContaMockDTO(conta.id(), novoSaldo, conta.limite());
                contasClient.atualizarSaldo(evento.usuarioId(), novaConta);

                transacaoDomain.atualizarStatus(StatusTransacao.APPROVED);
                System.out.println("Transação APROVADA!");
            }

        } catch (Exception e) {
            System.err.println("Erro técnico ao processar: " + e.getMessage());
            transacaoDomain.atualizarStatus(StatusTransacao.REJECTED);
        }

        entity.setStatus(transacaoDomain.getStatus());

        repository.save(entity);
    }
}