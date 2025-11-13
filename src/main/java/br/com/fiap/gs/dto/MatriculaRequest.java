package br.com.fiap.gs.dto;

import jakarta.validation.constraints.NotNull;

public class MatriculaRequest {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "ID da trilha é obrigatório")
    private Long trilhaId;

    private String status = "ATIVA"; // Valor padrão

    public MatriculaRequest() {}

    public MatriculaRequest(Long usuarioId, Long trilhaId, String status) {
        this.usuarioId = usuarioId;
        this.trilhaId = trilhaId;
        this.status = status;
    }

    // Getters e Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getTrilhaId() { return trilhaId; }
    public void setTrilhaId(Long trilhaId) { this.trilhaId = trilhaId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}