package com.ongreport.narraterpro.controller;

import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos os usuários.
     * Mapeado para GET /usuarios
     */
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios-lista"; // Retorna a view usuarios-lista.html
    }

    /**
     * Exibe o formulário para criar um novo usuário.
     * Mapeado para GET /usuarios/novo
     */
// Lógica correta no UsuarioController.java
    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        // 1. Esta linha cria um objeto 'Usuario' COMPLETAMENTE NOVO e VAZIO.
        model.addAttribute("usuario", new Usuario());

        // 2. Esta linha busca as cidades para popular o dropdown.
        model.addAttribute("todasCidades", usuarioService.findAllCidades());

        // 3. O formulário recebe o objeto vazio e, portanto, abre com os campos em branco.
        return "usuario-form";
    }

    /**
     * Exibe o formulário para editar um usuário existente.
     * Mapeado para GET /usuarios/editar/{id}
     */
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado!");
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuarioOptional.get());
        model.addAttribute("todasCidades", usuarioService.findAllCidades());
        return "usuario-form";
    }
/*
    @GetMapping("/perfil/{id}")
    public String exibirPerfil(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado!");
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuarioOptional.get());
        model.addAttribute("todasCidades", usuarioService.findAllCidades());
        return "usuario-form";
    }*/

    @GetMapping("/perfil") // <-- MUDANÇA 1: A URL agora é simples, sem o /{id}
    public String exibirMeuPerfil(Model model, Principal principal) { // <-- MUDANÇA 2: Não recebe mais o ID, e sim o Principal

        // 1. Pega o email (username) do usuário que está autenticado
        String email = principal.getName();

        // 2. Busca o usuário completo no banco de dados usando o email
        // (Você precisará ter um método findByEmail no seu UsuarioService)
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email);

        // 3. Verifica se o usuário foi encontrado
        // (Um usuário logado DEVE existir no banco, então um erro aqui seria grave)
        if (usuarioOptional.isEmpty()) {
            // Em um caso real, isso indicaria um problema sério.
            // Redirecionar para o logout é uma opção segura.
            return "redirect:/logout?error";
        }

        // 4. Adiciona o usuário encontrado ao model para o formulário usar
        model.addAttribute("usuario", usuarioOptional.get());
        model.addAttribute("todasCidades", usuarioService.findAllCidades());

        // 5. Renderiza o mesmo formulário de antes
        return "usuario-form";
    }

    /**
     * Salva um usuário (novo ou existente).
     * Mapeado para POST /usuarios/salvar
     */
    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioService.save(usuario);
        System.out.println("Usuario Controller ✔️");
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário salvo com sucesso!");

        return "redirect:/usuarios";
    }

    /**
     * Deleta um usuário.
     * Mapeado para GET /usuarios/deletar/{id}
     */
    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir usuário: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}