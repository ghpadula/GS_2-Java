package br.com.fiap.gs.service;

import br.com.fiap.gs.model.Matricula;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.model.Trilha;
import br.com.fiap.gs.repository.MatriculaRepository;
import br.com.fiap.gs.repository.TrilhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private TrilhaRepository trilhaRepository;

    public Matricula matricularUsuario(Usuario usuario, Long trilhaId) {
        Optional<Trilha> trilhaOpt = trilhaRepository.findById(trilhaId);
        if (trilhaOpt.isEmpty()) {
            throw new RuntimeException("Trilha não encontrada");
        }

        Trilha trilha = trilhaOpt.get();


        boolean jaMatriculado = matriculaRepository.existsByUsuarioAndTrilha(usuario, trilha);
        if (jaMatriculado) {
            throw new RuntimeException("Usuário já está matriculado nesta trilha");
        }

        Matricula matricula = new Matricula();
        matricula.setUsuario(usuario);
        matricula.setTrilha(trilha);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("ATIVA");

        return matriculaRepository.save(matricula);
    }

    public List<Matricula> listarMatriculasDoUsuario(Usuario usuario) {
        return matriculaRepository.findByUsuario(usuario);
    }

    public List<Matricula> listarMatriculasPorTrilha(Trilha trilha) {
        return matriculaRepository.findByTrilha(trilha);
    }

    public void cancelarMatricula(Long matriculaId, Usuario usuario) {
        Optional<Matricula> matriculaOpt = matriculaRepository.findById(matriculaId);

        if (matriculaOpt.isPresent()) {
            Matricula matricula = matriculaOpt.get();


            if (!matricula.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Esta matrícula não pertence ao usuário");
            }

            matricula.setStatus("CANCELADA");
            matriculaRepository.save(matricula);
        } else {
            throw new RuntimeException("Matrícula não encontrada");
        }
    }

    public void concluirMatricula(Long matriculaId, Usuario usuario) {
        Optional<Matricula> matriculaOpt = matriculaRepository.findById(matriculaId);

        if (matriculaOpt.isPresent()) {
            Matricula matricula = matriculaOpt.get();

            if (!matricula.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Esta matrícula não pertence ao usuário");
            }

            matricula.setStatus("CONCLUIDA");
            matriculaRepository.save(matricula);
        } else {
            throw new RuntimeException("Matrícula não encontrada");
        }
    }

    public long contarMatriculasAtivasDoUsuario(Usuario usuario) {
        return matriculaRepository.countByUsuarioAndStatus(usuario, "ATIVA");
    }

    public long contarMatriculasConcluidasDoUsuario(Usuario usuario) {
        return matriculaRepository.countByUsuarioAndStatus(usuario, "CONCLUIDA");
    }

    public void deleteById(Long id) {
        matriculaRepository.deleteById(id);
    }
    public boolean existsById(Long id) {
        return matriculaRepository.existsById(id);
    }
}