package br.com.beca.ms_transacoes.application.service;

import br.com.beca.ms_transacoes.application.services.RelatorioService;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import br.com.beca.ms_transacoes.infra.persistence.entities.TransacaoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @InjectMocks
    private RelatorioService service;

    @Mock
    private TransacaoRepository repository;

    @Test
    void deveGerarRelatorioDiario() {
        when(repository.buscarPorPeriodo(any(), any(), any()))
                .thenReturn(List.of(criarTransacao()));

        byte[] resultado = service.gerarRelatorioExcel(1L, "diario");

        assertNotNull(resultado);
        assertTrue(resultado.length > 0);
    }

    @Test
    void deveFalharQuandoPeriodoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.gerarRelatorioExcel(1L, "anual"));
    }

    private TransacaoEntity criarTransacao() {
        TransacaoEntity t = new TransacaoEntity();
        t.setValor(BigDecimal.TEN);
        t.setDataCriacao(LocalDateTime.now());
        return t;
    }
}
