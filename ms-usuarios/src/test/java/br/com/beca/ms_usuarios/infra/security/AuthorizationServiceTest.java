package br.com.beca.ms_usuarios.infra.security;

import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock UsuarioRepository repository;
    @InjectMocks AuthorizationService service;

    @Test
    void deveRetornarUserDetails_quandoUsuarioAtivo() {
        var usuario = new UsuarioEntity();
        usuario.setEmail("ana@email.com");
        usuario.setSenha("x");
        usuario.setAtivo(true);

        when(repository.findByEmailAndAtivoTrue("ana@email.com"))
                .thenReturn(Optional.of(usuario));

        var details = service.loadUserByUsername("ana@email.com");
        assertThat(details.getUsername()).isEqualTo("ana@email.com");
    }

    @Test
    void deveFalhar_quandoUsuarioInativoOuInexistente() {
        when(repository.findByEmailAndAtivoTrue("x@email.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("x@email.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
