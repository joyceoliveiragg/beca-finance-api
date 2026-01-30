package br.com.beca.ms_processor.infra.persistence.entities;

import br.com.beca.ms_processor.domain.entities.StatusTransacao;
import br.com.beca.ms_processor.domain.entities.Transacao;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private BigDecimal valor;
    private String moeda;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    private LocalDateTime dataCriacao;

    public static TransacaoEntity fromDomain(Transacao transacao) {
        return new TransacaoEntity(
                transacao.getId(),
                transacao.getUsuarioId(),
                transacao.getValor(),
                transacao.getMoeda(),
                transacao.getStatus(),
                transacao.getDataCriacao()
        );
    }
    
    public Transacao toDomain() {
        return new Transacao(
                this.id,
                this.usuarioId,
                this.valor,
                this.moeda,
                this.status,
                this.dataCriacao
        );
    }
}