package com.ongreport.narraterpro.controller;

import com.ongreport.narraterpro.entity.Projeto;
import com.ongreport.narraterpro.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @Autowired
    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping
    public String listarProjetos(Model model) {
        model.addAttribute("projetos", projetoService.findAll());
        return "projetos-lista";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("projeto", new Projeto());
        return "projeto-form";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Projeto> projetoOptional = projetoService.findById(id);
        if (projetoOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Projeto não encontrado!");
            return "redirect:/projetos";
        }
        model.addAttribute("projeto", projetoOptional.get());
        return "projeto-form";
    }

    @PostMapping("/salvar")
    public String salvarProjeto(@ModelAttribute("projeto") Projeto projeto, RedirectAttributes redirectAttributes) {
        projetoService.save(projeto);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto salvo com sucesso!");
        return "redirect:/projetos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarProjeto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            projetoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir projeto. Verifique se ele não está sendo usado em alguma atividade.");
        }
        return "redirect:/projetos";
    }
}