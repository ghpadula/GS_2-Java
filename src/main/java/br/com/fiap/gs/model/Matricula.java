package br.com.fiap.gs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Trilha é obrigatória")
    @ManyToOne
    @JoinColumn(name = "trilha_id", nullable = false)
    private Trilha trilha;

    @NotNull(message = "Data de inscrição é obrigatória")
    @Column(name = "data_inscricao", nullable = false)
    private LocalDate dataInscricao;

    @NotBlank(message = "Status é obrigatório")
    @Column(nullable = false, length = 50)
    private String status;

    public Matricula() {}

    public Matricula(Usuario usuario, Trilha trilha, String status) {
        this.usuario = usuario;
        this.trilha = trilha;
        this.status = status;
        this.dataInscricao = LocalDate.now();
    }

    @PrePersist
    protected void onCreate() {
        if (dataInscricao == null) {
            dataInscricao = LocalDate.now();
        }
        if (status == null) {
            status = "ATIVA";
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Trilha getTrilha() { return trilha; }
    public void setTrilha(Trilha trilha) { this.trilha = trilha; }

    public LocalDate getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDate dataInscricao) { this.dataInscricao = dataInscricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}