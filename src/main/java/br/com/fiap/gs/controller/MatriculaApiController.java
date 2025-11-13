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
@RequestMapping("/api/matriculas" )
public class MatriculaApiController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/usuario/{usuarioId}/trilha/{trilhaId}")
    public ResponseEntity<Matricula> matricularUsuario(
            @PathVariable Long usuarioId,
            @PathVariable Long trilhaId) {

        Usuario usuario = usuarioService.findOrThrow(usuarioId);

        Matricula matricula = matriculaService.matricularUsuario(usuario, trilhaId);

        return ResponseEntity.status(201).body(matricula);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Matricula>> listarMatriculasUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.findOrThrow(usuarioId);

        List<Matricula> matriculas = matriculaService.listarMatriculasDoUsuario(usuario);
        return ResponseEntity.ok(matriculas);
    }

    @PutMapping("/{matriculaId}/cancelar")
    public ResponseEntity<String> cancelarMatricula(
            @PathVariable Long matriculaId,
            @RequestParam Long usuarioId) {

        Usuario usuario = usuarioService.findOrThrow(usuarioId);

        matriculaService.cancelarMatricula(matriculaId, usuario);
        return ResponseEntity.ok("Matrícula cancelada com sucesso");
    }

    @PutMapping("/{matriculaId}/concluir")
    public ResponseEntity<String> concluirMatricula(
            @PathVariable Long matriculaId,
            @RequestParam Long usuarioId) {

        Usuario usuario = usuarioService.findOrThrow(usuarioId);

        matriculaService.concluirMatricula(matriculaId, usuario);
        return ResponseEntity.ok("Matrícula concluída com sucesso");
    }

    @GetMapping("/usuario/{usuarioId}/estatisticas")
    public ResponseEntity<EstatisticasResponse> getEstatisticasUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.findOrThrow(usuarioId);

        long ativas = matriculaService.contarMatriculasAtivasDoUsuario(usuario);
        long concluidas = matriculaService.contarMatriculasConcluidasDoUsuario(usuario);

        var estatisticas = new EstatisticasResponse(ativas, concluidas);
        return ResponseEntity.ok(estatisticas);
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
    public ResponseEntity<Void> deletarMatricula(@PathVariable Long id) {
        if (!matriculaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        matriculaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
