package br.com.beca.ms_processor.infra.persistence;

import br.com.beca.ms_processor.infra.persistence.entities.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {
}