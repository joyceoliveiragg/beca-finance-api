//package br.com.beca.ms_usuarios.application.usecases;
//
//import br.com.beca.ms_usuarios.domain.entities.Usuario;
//import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//@Service
//public class DebitarSaldoUseCase {
//
//    @Autowired
//    private UsuarioRepository repository;
//
//    @Transactional
//    public void executar(Long id, BigDecimal valor) {
//        Usuario usuario = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        if (usuario.getSaldo().compareTo(valor) < 0) {
//            throw new RuntimeException("Saldo insuficiente");
//        }
//
//        usuario.setSaldo(usuario.getSaldo().subtract(valor));
//
//        repository.save(usuario);
//    }
//}