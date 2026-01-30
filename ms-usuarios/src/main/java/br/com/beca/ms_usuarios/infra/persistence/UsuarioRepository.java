package br.com.beca.ms_usuarios.infra.persistence;

import br.com.beca.ms_usuarios.domain.entities.Usuario;
import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
