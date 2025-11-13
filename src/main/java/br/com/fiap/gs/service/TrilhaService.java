package br.com.fiap.gs.service;

import br.com.fiap.gs.model.Trilha;
import br.com.fiap.gs.repository.TrilhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrilhaService {

    @Autowired
    private TrilhaRepository trilhaRepository;

    public List<Trilha> findAll() {
        return trilhaRepository.findAll();
    }

    public Optional<Trilha> findById(Long id) {
        return trilhaRepository.findById(id);
    }

    public Trilha save(Trilha trilha) {
        return trilhaRepository.save(trilha);
    }

    public void deleteById(Long id) {
        trilhaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return trilhaRepository.existsById(id);
    }


    public List<Trilha> findByNivel(String nivel) {
        return trilhaRepository.findByNivel(nivel);
    }

    public List<Trilha> findByFocoPrincipal(String focoPrincipal) {
        return trilhaRepository.findByFocoPrincipal(focoPrincipal);
    }

    public List<Trilha> findByNome(String nome) {
        return trilhaRepository.findByNome(nome);
    }

    public List<Trilha> findByNomeContainingIgnoreCase(String nome) {
        return trilhaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Trilha> findFirstByNome(String nome) {
        return trilhaRepository.findFirstByNome(nome);
    }
}