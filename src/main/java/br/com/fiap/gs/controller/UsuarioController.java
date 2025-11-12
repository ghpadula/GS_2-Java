package br.com.fiap.gs.controller;

import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarUsuarios(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "usuarios";
    }

    @GetMapping("/{id}")
    public String detalhesUsuario(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/usuarios";
        }

        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "detalhes-usuario";
    }

    @GetMapping("/novo")
    public String formNovoUsuario(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "form-usuario";
    }

    @PostMapping
    public String criarUsuario(@Valid @ModelAttribute Usuario usuario,
                               BindingResult result,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        System.out.println("=== TENTANDO CRIAR USUÁRIO ===");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("Área: " + usuario.getAreaAtuacao());
        System.out.println("Nível: " + usuario.getNivelCarreira());

        // Validar se email já existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            result.rejectValue("email", "error.usuario", "Email já cadastrado");
            System.out.println("ERRO: Email já existe");
        }

        if (result.hasErrors()) {
            System.out.println("ERROS DE VALIDAÇÃO:");
            result.getAllErrors().forEach(error -> System.out.println("- " + error.getDefaultMessage()));
            model.addAttribute("usuarioLogado", usuarioLogado);
            return "form-usuario";
        }

        try {
            usuario.setAdmin(false);
            usuarioRepository.save(usuario);
            System.out.println("USUÁRIO SALVO COM SUCESSO! ID: " + usuario.getId());

            redirectAttributes.addFlashAttribute("mensagem", "Usuário criado com sucesso!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            System.out.println("ERRO AO SALVAR: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar usuário: " + e.getMessage());
            return "redirect:/usuarios/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String formEditarUsuario(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/usuarios";
        }

        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "form-usuario";
    }

    @PostMapping("/{id}")
    public String atualizarUsuario(@PathVariable Long id,
                                   @Valid @ModelAttribute Usuario usuario,
                                   BindingResult result,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(id)) {
            result.rejectValue("email", "error.usuario", "Email já cadastrado");
        }

        if (result.hasErrors()) {
            model.addAttribute("usuarioLogado", usuarioLogado);
            return "form-usuario";
        }

        Optional<Usuario> usuarioOriginalOpt = usuarioRepository.findById(id);
        if (usuarioOriginalOpt.isPresent()) {
            usuario.setAdmin(usuarioOriginalOpt.get().isAdmin());
        }

        usuario.setId(id);
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/deletar")
    public String excluirUsuario(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        if (usuarioLogado.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("erro", "Você não pode excluir sua própria conta!");
            return "redirect:/usuarios";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            usuarioRepository.delete(usuarioOpt.get());
            redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        }

        return "redirect:/usuarios";
    }


}