package br.com.beca.ms_processor.infra.kafka;

import br.com.beca.ms_processor.application.dto.TransacaoEvento;
import br.com.beca.ms_processor.domain.entities.StatusTransacao;
import br.com.beca.ms_processor.infra.feign.CambioClient;
import br.com.beca.ms_processor.infra.feign.ContaMockDTO;
import br.com.beca.ms_processor.infra.feign.ContasClient;
import br.com.beca.ms_processor.infra.feign.CambioDTO;
import br.com.beca.ms_processor.infra.persistence.TransacaoRepository;
import br.com.beca.ms_processor.infra.persistence.entities.TransacaoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @InjectMocks
    private KafkaConsumerService consumer;

    @Mock
    private TransacaoRepository repository;

    @Mock
    private ContasClient contasClient;

    @Mock
    private CambioClient cambioClient;

    @Test
    void deveRejeitarTransacaoEmUSDQuandoSaldoInsuficiente() {
        TransacaoEntity entity = new TransacaoEntity();
        entity.setId(2L);
        entity.setUsuarioId(1L);
        entity.setValor(BigDecimal.valueOf(50));
        entity.setMoeda("USD");

        when(repository.findById(2L)).thenReturn(Optional.of(entity));

        when(cambioClient.buscarCotacao("USD"))
                .thenReturn(new CambioDTO(
                        "USD",
                        "Dólar Americano",
                        5.0,
                        LocalDateTime.now()
                ));


        when(contasClient.buscarConta(1L))
                .thenReturn(new ContaMockDTO(
                        1L,
                        BigDecimal.valueOf(100),
                        BigDecimal.ZERO
                ));

        TransacaoEvento evento =
                new TransacaoEvento(2L, 1L, BigDecimal.valueOf(50), "USD");

        consumer.consumir(evento);

        verify(repository).save(argThat(t ->
                t.getStatus() == StatusTransacao.REJECTED
        ));
    }

}
