package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

    List<Competencia> findByCategoria(String categoria);

    List<Competencia> findByNome(String nome);
}