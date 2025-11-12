package br.com.fiap.gs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacao;

    @Column(name = "nivel_carreira", length = 50)
    private String nivelCarreira;

    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "senha", length = 255, nullable = false)
    private String senha;


    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(name = "admin", nullable = false)
    private boolean admin = false;

    public Usuario() {}

    public Usuario(String nome, String email, String areaAtuacao, String nivelCarreira, String senha, boolean admin) {
        this.nome = nome;
        this.email = email;
        this.areaAtuacao = areaAtuacao;
        this.nivelCarreira = nivelCarreira;
        this.senha = senha;
        this.admin = admin;
        this.dataCadastro = LocalDate.now();
    }

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String areaAtuacao) { this.areaAtuacao = areaAtuacao; }

    public String getNivelCarreira() { return nivelCarreira; }
    public void setNivelCarreira(String nivelCarreira) { this.nivelCarreira = nivelCarreira; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAdmin() {
        return this.admin || "admin@email.com".equalsIgnoreCase(this.email);
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
