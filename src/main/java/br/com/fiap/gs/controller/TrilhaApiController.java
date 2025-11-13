package br.com.fiap.gs.controller;

import br.com.fiap.gs.model.Trilha;
import br.com.fiap.gs.service.TrilhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trilhas")
public class TrilhaApiController {

    @Autowired
    private TrilhaService trilhaService;

    @GetMapping
    public ResponseEntity<List<Trilha>> listarTodas() {
        List<Trilha> trilhas = trilhaService.findAll();
        return ResponseEntity.ok(trilhas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trilha> buscarPorId(@PathVariable Long id) {
        Trilha trilha = trilhaService.findOrThrow(id);
        return ResponseEntity.ok(trilha);
    }

    @PostMapping
    public ResponseEntity<Trilha> criar(@RequestBody Trilha trilha) {
        Trilha trilhaSalva = trilhaService.save(trilha);
        return ResponseEntity.status(201).body(trilhaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trilha> atualizar(@PathVariable Long id, @RequestBody Trilha trilha) {
        if (!trilhaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        trilha.setId(id);
        Trilha trilhaAtualizada = trilhaService.save(trilha);
        return ResponseEntity.ok(trilhaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!trilhaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        trilhaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}