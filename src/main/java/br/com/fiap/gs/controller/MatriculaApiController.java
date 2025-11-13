package br.com.fiap.gs.controller;

import br.com.fiap.gs.model.Matricula;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.service.MatriculaService;
import br.com.fiap.gs.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaApiController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/usuario/{usuarioId}/trilha/{trilhaId}")
    public ResponseEntity<?> matricularUsuario(
            @PathVariable Long usuarioId,
            @PathVariable Long trilhaId) {

        try {

            Usuario usuario = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


            Matricula matricula = matriculaService.matricularUsuario(usuario, trilhaId);
            return ResponseEntity.status(201).body(matricula);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarMatriculasUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Matricula> matriculas = matriculaService.listarMatriculasDoUsuario(usuario);
            return ResponseEntity.ok(matriculas);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{matriculaId}/cancelar")
    public ResponseEntity<?> cancelarMatricula(
            @PathVariable Long matriculaId,
            @RequestParam Long usuarioId) {

        try {
            Usuario usuario = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            matriculaService.cancelarMatricula(matriculaId, usuario);
            return ResponseEntity.ok("Matrícula cancelada com sucesso");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{matriculaId}/concluir")
    public ResponseEntity<?> concluirMatricula(
            @PathVariable Long matriculaId,
            @RequestParam Long usuarioId) {

        try {
            Usuario usuario = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            matriculaService.concluirMatricula(matriculaId, usuario);
            return ResponseEntity.ok("Matrícula concluída com sucesso");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}/estatisticas")
    public ResponseEntity<?> getEstatisticasUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            long ativas = matriculaService.contarMatriculasAtivasDoUsuario(usuario);
            long concluidas = matriculaService.contarMatriculasConcluidasDoUsuario(usuario);

            var estatisticas = new EstatisticasResponse(ativas, concluidas);
            return ResponseEntity.ok(estatisticas);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public static class EstatisticasResponse {
        private long matriculasAtivas;
        private long matriculasConcluidas;

        public EstatisticasResponse(long ativas, long concluidas) {
            this.matriculasAtivas = ativas;
            this.matriculasConcluidas = concluidas;
        }


        public long getMatriculasAtivas() { return matriculasAtivas; }
        public long getMatriculasConcluidas() { return matriculasConcluidas; }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMatricula(@PathVariable Long id) {
        try {
            if (!matriculaService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            matriculaService.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}