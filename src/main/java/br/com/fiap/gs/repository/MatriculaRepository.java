package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Matricula;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.model.Trilha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByTrilhaId(Long trilhaId);
    boolean existsByUsuarioIdAndTrilhaId(Long usuarioId, Long trilhaId);
    long countByUsuarioId(Long usuarioId);

    List<Matricula> findByUsuario(Usuario usuario);
    List<Matricula> findByTrilha(Trilha trilha);
    boolean existsByUsuarioAndTrilha(Usuario usuario, Trilha trilha);
    long countByUsuarioAndStatus(Usuario usuario, String status);
    long countByUsuario(Usuario usuario); // MÉTODO QUE ESTAVA FALTANDO 🆕

    @Query("SELECT m FROM Matricula m WHERE m.usuario = :usuario AND m.status = :status")
    List<Matricula> findByUsuarioAndStatus(@Param("usuario") Usuario usuario, @Param("status") String status);

    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.trilha = :trilha")
    long countByTrilha(@Param("trilha") Trilha trilha);
}