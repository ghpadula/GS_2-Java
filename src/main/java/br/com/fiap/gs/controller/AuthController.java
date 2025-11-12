package br.com.fiap.gs.controller;

import br.com.fiap.gs.dto.LoginRequest;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.repository.UsuarioRepository;
import br.com.fiap.gs.repository.TrilhaRepository;
import br.com.fiap.gs.repository.CompetenciaRepository;
import br.com.fiap.gs.repository.MatriculaRepository;
import br.com.fiap.gs.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;


    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
        Usuario usuario = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("erro", "Email ou senha inválidos!");
            return "login";
        }
    }


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Verificar se usuário está logado
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Estatísticas focadas em aprendizado
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalTrilhas", trilhaRepository.count());
        model.addAttribute("totalCompetencias", competenciaRepository.count());


        long minhasMatriculasCount = matriculaRepository.countByUsuarioId(usuario.getId());
        model.addAttribute("minhasMatriculas", minhasMatriculasCount);

        return "dashboard";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
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