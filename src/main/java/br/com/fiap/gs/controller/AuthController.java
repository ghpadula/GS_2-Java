package br.com.fiap.gs.controller;

import br.com.fiap.gs.dto.LoginRequest;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession session) {
        Usuario usuario = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuario != null) {
            // Login bem-sucedido
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/dashboard";
        } else {
            // Login falhou
            return "redirect:/login?erro=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginApi(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Usuario usuario = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario);
            return ResponseEntity.ok().body("Login realizado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }
}