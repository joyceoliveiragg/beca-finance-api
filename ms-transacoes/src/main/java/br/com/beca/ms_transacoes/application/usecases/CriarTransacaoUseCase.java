package br.com.beca.ms_transacoes.application.usecases;

import br.com.beca.ms_transacoes.application.dto.TransacaoEvento;
import br.com.beca.ms_transacoes.domain.entities.CategoriaTransacao;
import br.com.beca.ms_transacoes.domain.entities.Transacao;
import br.com.beca.ms_transacoes.infra.kafka.KafkaProducerService;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import br.com.beca.ms_transacoes.infra.persistence.entities.TransacaoEntity;
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
    public void executar(Long usuarioId,
                         BigDecimal valor,
                         String moeda,
                         CategoriaTransacao categoria) {

        Transacao transacaoDomain =
                new Transacao(usuarioId, valor, moeda, categoria);

        TransacaoEntity entity = new TransacaoEntity(transacaoDomain);

        entity = repository.save(entity);

        transacaoDomain.setId(entity.getId());

        kafkaService.enviarEvento(
                TransacaoEvento.fromEntity(transacaoDomain)
        );
    }
}