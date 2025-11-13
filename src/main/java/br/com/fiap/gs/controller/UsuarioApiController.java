package br.com.fiap.gs.controller;

import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.findOrThrow(id);
        return ResponseEntity.ok(usuario);
    }


    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setAdmin(false);
        Usuario usuarioSalvo = usuarioService.save(usuario);
        return ResponseEntity.status(201).body(usuarioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (!usuarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var usuarioComEmail = usuarioService.findByEmail(usuario.getEmail());
        if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setId(id);
        Usuario usuarioAtualizado = usuarioService.save(usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!usuarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}