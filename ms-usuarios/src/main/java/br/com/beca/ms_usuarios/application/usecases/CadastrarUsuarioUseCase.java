package br.com.beca.ms_usuarios.application.usecases;

import br.com.beca.ms_usuarios.application.dto.CadastroUsuarioRequest;
import br.com.beca.ms_usuarios.application.dto.UsuarioResponse;
import br.com.beca.ms_usuarios.domain.entities.Usuario;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CadastrarUsuarioUseCase {

    @Autowired
    private UsuarioRepository repository;

    @Transactional
    public UsuarioResponse executar(CadastroUsuarioRequest dados) {
        Usuario usuario = new Usuario(
                dados.nome(),
                dados.email(),
                dados.senha(),
                dados.cpf(),
                dados.saldo()
        );
        repository.save(usuario);
        return new UsuarioResponse(usuario);
    }
}