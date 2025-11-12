package br.com.fiap.gs.controller;

import br.com.fiap.gs.model.Matricula;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.service.MatriculaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping("/minhas")
    public String minhasMatriculas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Matricula> matriculas = matriculaService.listarMatriculasDoUsuario(usuario);
        long totalAtivas = matriculaService.contarMatriculasAtivasDoUsuario(usuario);
        long totalConcluidas = matriculaService.contarMatriculasConcluidasDoUsuario(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("matriculas", matriculas);
        model.addAttribute("totalAtivas", totalAtivas);
        model.addAttribute("totalConcluidas", totalConcluidas);
        model.addAttribute("totalMatriculas", totalAtivas);

        return "minhas-matriculas";
    }

    @PostMapping("/{trilhaId}/matricular")
    public String matricular(@PathVariable Long trilhaId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            Matricula matricula = matriculaService.matricularUsuario(usuario, trilhaId);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Matrícula realizada com sucesso na trilha: " + matricula.getTrilha().getNome());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/trilhas/" + trilhaId;
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarMatricula(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            matriculaService.cancelarMatricula(id, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Matrícula cancelada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/matriculas/minhas";
    }

    @PostMapping("/{id}/concluir")
    public String concluirMatricula(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            matriculaService.concluirMatricula(id, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Parabéns! Trilha concluída com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/matriculas/minhas";
    }
}