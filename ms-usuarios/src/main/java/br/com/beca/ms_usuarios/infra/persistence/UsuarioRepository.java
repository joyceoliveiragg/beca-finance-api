package br.com.beca.ms_usuarios.infra.persistence;

import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmailAndAtivoTrue(String email);

    Optional<UsuarioEntity> findByIdAndAtivoTrue(Long id);

    List<UsuarioEntity> findAllByAtivoTrue();

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
