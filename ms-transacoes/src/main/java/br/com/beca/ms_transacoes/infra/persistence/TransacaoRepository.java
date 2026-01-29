package br.com.beca.ms_transacoes.infra.persistence;

import br.com.beca.ms_transacoes.domain.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioId(Long usuarioId);
    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.usuarioId = :usuarioId")
    BigDecimal calcularTotalGasto(@Param("usuarioId") Long usuarioId);
}