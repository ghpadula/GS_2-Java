package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Trilha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrilhaRepository extends JpaRepository<Trilha, Long> {

    List<Trilha> findByNivel(String nivel);
    List<Trilha> findByFocoPrincipal(String focoPrincipal);
    List<Trilha> findByNome(String nome);
    List<Trilha> findByNomeContainingIgnoreCase(String nome);
    Optional<Trilha> findFirstByNome(String nome);
}