package br.com.fiap.gs.exception;

public class TrailNotFoundException extends RuntimeException {
    public TrailNotFoundException(Long id) {
        super("Trilha com ID " + id + " não encontrada.");
    }
}
