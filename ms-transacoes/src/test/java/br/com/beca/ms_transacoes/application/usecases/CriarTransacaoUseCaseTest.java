package br.com.beca.ms_transacoes.application.usecases;

import br.com.beca.ms_transacoes.application.dto.TransacaoEvento;
import br.com.beca.ms_transacoes.infra.kafka.KafkaProducerService;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import br.com.beca.ms_transacoes.infra.persistence.entities.TransacaoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarTransacaoUseCaseTest {

    @InjectMocks
    private CriarTransacaoUseCase useCase;

    @Mock
    private TransacaoRepository repository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Test
    void deveCriarTransacaoEEnviarEventoKafka() {

        when(repository.save(any()))
                .thenAnswer(invocation -> {
                    TransacaoEntity entity = invocation.getArgument(0);
                    entity.setId(1L);
                    return entity;
                });

        useCase.executar(
                1L,
                BigDecimal.valueOf(100),
                "BRL",
                "ALIMENTACAO"
        );

        verify(repository, times(1)).save(any(TransacaoEntity.class));
        verify(kafkaProducerService, times(1)).enviarEvento(any(TransacaoEvento.class));
    }
}
