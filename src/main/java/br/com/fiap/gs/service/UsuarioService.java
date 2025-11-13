package br.com.fiap.gs.service;

import br.com.fiap.gs.exception.UserNotFoundException;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {



    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        findOrThrow(id);
        usuarioRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return usuarioRepository.existsById(id);
    }

    public long countTotalUsuarios() {
        return usuarioRepository.countTotalUsuarios();
    }


    public Usuario findOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

}