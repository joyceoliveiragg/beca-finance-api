package br.com.beca.ms_transacoes.infra.persistence;

import br.com.beca.ms_transacoes.domain.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {}