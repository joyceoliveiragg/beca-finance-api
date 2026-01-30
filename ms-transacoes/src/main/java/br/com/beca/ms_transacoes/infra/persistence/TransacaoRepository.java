package br.com.beca.ms_transacoes.infra.persistence;

import br.com.beca.ms_transacoes.infra.persistence.entities.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

    List<TransacaoEntity> findByUsuarioId(Long usuarioId);
    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM TransacaoEntity t WHERE t.usuarioId = :usuarioId")
    BigDecimal calcularTotalGasto(@Param("usuarioId") Long usuarioId);
    @Query("""
    SELECT t FROM TransacaoEntity t
    WHERE t.usuarioId = :usuarioId
    AND t.dataCriacao BETWEEN :inicio AND :fim
""")
    List<TransacaoEntity> buscarPorPeriodo(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

}