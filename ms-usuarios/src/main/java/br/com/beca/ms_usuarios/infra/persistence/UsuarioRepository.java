package br.com.beca.ms_usuarios.infra.persistence;

import br.com.beca.ms_usuarios.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}