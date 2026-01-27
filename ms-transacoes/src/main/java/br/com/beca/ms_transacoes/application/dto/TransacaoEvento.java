package br.com.beca.ms_transacoes.application.dto;

import br.com.beca.ms_transacoes.domain.entities.Transacao;
import java.math.BigDecimal;


public record TransacaoEvento(
        Long id,
        Long usuarioId,
        BigDecimal valor,
        String moeda,
        String status
) {
    public static TransacaoEvento fromEntity(Transacao t) {
        return new TransacaoEvento(
                t.getId(), t.getUsuarioId(), t.getValor(), t.getMoeda(), t.getStatus().toString()
        );
    }
}