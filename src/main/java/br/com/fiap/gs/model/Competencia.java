package br.com.fiap.gs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competencias")
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToMany(mappedBy = "competencias")
    private List<Trilha> trilhas = new ArrayList<>();

    public Competencia() {}

    public Competencia(String nome, String categoria, String descricao) {
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Trilha> getTrilhas() { return trilhas; }
    public void setTrilhas(List<Trilha> trilhas) { this.trilhas = trilhas; }
}