package br.com.beca.ms_transacoes.infra.kafka;

import br.com.beca.ms_transacoes.application.dto.TransacaoEvento;
import br.com.beca.ms_transacoes.domain.entities.StatusTransacao;
import br.com.beca.ms_transacoes.domain.entities.Transacao;
import br.com.beca.ms_transacoes.infra.feign.ContaMockDTO;
import br.com.beca.ms_transacoes.infra.feign.ContasClient;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaConsumerService {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private ContasClient contasClient;

    @KafkaListener(topics = "transacao-criada", groupId = "beca-group")
    public void consumir(TransacaoEvento evento) {

        Transacao transacao = repository.findById(evento.id()).orElse(null);

        if (transacao == null) {
            System.out.println("Erro: Transação não encontrada no banco: " + evento.id());
            return;
        }
        try {
            ContaMockDTO conta = contasClient.buscarConta(evento.usuarioId());

            BigDecimal novoSaldo = conta.saldo().subtract(transacao.getValor());

            if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Saldo insuficiente no MockAPI");
            }

            ContaMockDTO novaConta = new ContaMockDTO(conta.id(), novoSaldo, conta.limite());
            contasClient.atualizarSaldo(evento.usuarioId(), novaConta);

            transacao.atualizarStatus(StatusTransacao.APPROVED);
            System.out.println("Transação APROVADA: " + transacao.getId());

        } catch (Exception e) {
            System.out.println("Erro ao processar: " + e.getMessage());
            transacao.atualizarStatus(StatusTransacao.REJECTED);
        }
        repository.save(transacao);
    }
}