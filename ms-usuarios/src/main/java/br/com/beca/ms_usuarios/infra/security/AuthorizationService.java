package br.com.beca.ms_usuarios.infra.security;

import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmailAndAtivoTrue(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado ou inativo")
                );
    }
}