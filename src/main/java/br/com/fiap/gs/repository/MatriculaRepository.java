package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByUsuarioId(Long usuarioId);
    List<Matricula> findByTrilhaId(Long trilhaId);
    Optional<Matricula> findByUsuarioIdAndTrilhaId(Long usuarioId, Long trilhaId);
    boolean existsByUsuarioIdAndTrilhaId(Long usuarioId, Long trilhaId);
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.usuario.id = :usuarioId")
    long countByUsuarioId(Long usuarioId);
}