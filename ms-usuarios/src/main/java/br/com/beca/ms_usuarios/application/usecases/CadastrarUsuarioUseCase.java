package br.com.beca.ms_usuarios.application.usecases;

import br.com.beca.ms_usuarios.application.dto.CadastroUsuarioRequest;
import br.com.beca.ms_usuarios.application.dto.UsuarioResponse;
import br.com.beca.ms_usuarios.domain.entities.Usuario;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastrarUsuarioUseCase {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponse executar(CadastroUsuarioRequest dados) {
        String senhaCriptografada = passwordEncoder.encode(dados.senha());

        Usuario usuarioDomain = new Usuario(
                null,
                dados.nome(),
                dados.email(),
                senhaCriptografada,
                dados.cpf()
        );

        UsuarioEntity entity = new UsuarioEntity(usuarioDomain);

        repository.save(entity);

        return new UsuarioResponse(entity.toDomain());
    }
}