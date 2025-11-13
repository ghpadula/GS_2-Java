package br.com.fiap.gs.controller;

import br.com.fiap.gs.model.Trilha;
import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.model.Competencia;
import br.com.fiap.gs.model.Matricula;
import br.com.fiap.gs.repository.TrilhaRepository;
import br.com.fiap.gs.repository.MatriculaRepository;
import br.com.fiap.gs.repository.CompetenciaRepository;
import br.com.fiap.gs.service.MatriculaService;
import br.com.fiap.gs.service.TrilhaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trilhas")
public class TrilhaController {

    @Autowired
    private TrilhaService trilhaService;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Autowired
    private MatriculaService matriculaService;

    private boolean isAdmin(Usuario usuario) {
        return usuario != null && usuario.isAdmin();
    }

    @GetMapping
    public String listarTrilhas(HttpSession session, Model model,
                                @RequestParam(required = false) String nivel,
                                @RequestParam(required = false) String foco) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Trilha> trilhas;

        if (nivel != null && !nivel.isEmpty()) {
            trilhas = trilhaService.findByNivel(nivel);
        } else if (foco != null && !foco.isEmpty()) {
            trilhas = trilhaService.findByFocoPrincipal(foco);
        } else {
            trilhas = trilhaService.findAll();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("trilhas", trilhas);
        model.addAttribute("filtroNivel", nivel);
        model.addAttribute("filtroFoco", foco);
        model.addAttribute("isAdmin", isAdmin(usuario));

        return "trilhas";
    }

    @GetMapping("/{id}")
    public String detalhesTrilha(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        Optional<Trilha> trilhaOpt = trilhaService.findById(id);

        if (trilhaOpt.isPresent()) {
            Trilha trilha = trilhaOpt.get();

            boolean jaMatriculado = matriculaRepository.existsByUsuarioIdAndTrilhaId(usuario.getId(), id);

            model.addAttribute("usuario", usuario);
            model.addAttribute("trilha", trilha);
            model.addAttribute("jaMatriculado", jaMatriculado);
            model.addAttribute("competencias", trilha.getCompetencias());
            model.addAttribute("isAdmin", isAdmin(usuario));

            return "detalhes-trilha";
        } else {
            return "redirect:/trilhas?erro=trilha_nao_encontrada";
        }
    }

    @GetMapping("/novo")
    public String formularioCriarTrilha(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!isAdmin(usuario)) {
            return "redirect:/trilhas?erro=acesso_negado";
        }

        List<Competencia> competencias = competenciaRepository.findAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("trilha", new Trilha());
        model.addAttribute("competencias", competencias);
        model.addAttribute("isAdmin", true);
        return "form-trilha";
    }

    @PostMapping
    public String criarTrilha(@ModelAttribute Trilha trilha,
                              @RequestParam(value = "competenciasIds", required = false) List<Long> competenciasIds,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!isAdmin(usuario)) {
            return "redirect:/trilhas?erro=acesso_negado";
        }

        try {
            if (competenciasIds != null && !competenciasIds.isEmpty()) {
                List<Competencia> competenciasSelecionadas = competenciaRepository.findAllById(competenciasIds);
                trilha.setCompetencias(competenciasSelecionadas);
            }

            trilhaService.save(trilha);
            redirectAttributes.addFlashAttribute("sucesso", "Trilha criada com sucesso!");
            return "redirect:/trilhas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar trilha: " + e.getMessage());
            return "redirect:/trilhas/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String formularioEditarTrilha(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!isAdmin(usuario)) {
            return "redirect:/trilhas?erro=acesso_negado";
        }

        Optional<Trilha> trilhaOpt = trilhaService.findById(id);

        if (trilhaOpt.isPresent()) {
            List<Competencia> competencias = competenciaRepository.findAll();

            model.addAttribute("usuario", usuario);
            model.addAttribute("trilha", trilhaOpt.get());
            model.addAttribute("competencias", competencias);
            model.addAttribute("isAdmin", true);
            return "form-trilha";
        } else {
            return "redirect:/trilhas?erro=trilha_nao_encontrada";
        }
    }

    @PostMapping("/{id}")
    public String atualizarTrilha(@PathVariable Long id,
                                  @ModelAttribute Trilha trilhaAtualizada,
                                  @RequestParam(value = "competenciasIds", required = false) List<Long> competenciasIds,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!isAdmin(usuario)) {
            return "redirect:/trilhas?erro=acesso_negado";
        }

        try {
            Optional<Trilha> trilhaOpt = trilhaService.findById(id);

            if (trilhaOpt.isPresent()) {
                Trilha trilhaExistente = trilhaOpt.get();

                trilhaExistente.setNome(trilhaAtualizada.getNome());
                trilhaExistente.setDescricao(trilhaAtualizada.getDescricao());
                trilhaExistente.setNivel(trilhaAtualizada.getNivel());
                trilhaExistente.setCargaHoraria(trilhaAtualizada.getCargaHoraria());
                trilhaExistente.setFocoPrincipal(trilhaAtualizada.getFocoPrincipal());

                if (competenciasIds != null && !competenciasIds.isEmpty()) {
                    List<Competencia> competenciasSelecionadas = competenciaRepository.findAllById(competenciasIds);
                    trilhaExistente.setCompetencias(competenciasSelecionadas);
                } else {
                    trilhaExistente.getCompetencias().clear();
                }

                trilhaService.save(trilhaExistente);
                redirectAttributes.addFlashAttribute("sucesso", "Trilha atualizada com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Trilha não encontrada!");
            }

            return "redirect:/trilhas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar trilha: " + e.getMessage());
            return "redirect:/trilhas/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/deletar")
    public String deletarTrilha(@PathVariable Long id, HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!isAdmin(usuario)) {
            return "redirect:/trilhas?erro=acesso_negado";
        }

        try {
            Optional<Trilha> trilhaOpt = trilhaService.findById(id);

            if (trilhaOpt.isPresent()) {
                List<Matricula> matriculas = matriculaRepository.findByTrilhaId(id);
                boolean temMatriculas = !matriculas.isEmpty();

                if (temMatriculas) {
                    redirectAttributes.addFlashAttribute("erro",
                            "Não é possível excluir a trilha pois existem matrículas ativas!");
                    System.out.println("DEBUG - Não pode excluir: tem " + matriculas.size() + " matrículas");
                } else {
                    trilhaService.deleteById(id);
                    redirectAttributes.addFlashAttribute("sucesso", "Trilha excluída com sucesso!");
                }
            } else {
                redirectAttributes.addFlashAttribute("erro", "Trilha não encontrada!");
            }

            return "redirect:/trilhas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir trilha: " + e.getMessage());
            return "redirect:/trilhas";
        }
    }

    @PostMapping("/{id}/matricular")
    public String matricular(@PathVariable Long id, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            Matricula matricula = matriculaService.matricularUsuario(usuario, id);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Matrícula realizada com sucesso na trilha: " + matricula.getTrilha().getNome());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/trilhas/" + id;
    }
}