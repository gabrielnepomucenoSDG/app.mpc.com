package com.ongreport.narraterpro.controller;


import com.ongreport.narraterpro.config.AppConfig;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import com.ongreport.narraterpro.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class GeneralController {

    private final UsuarioRepository usuarioRepository;
    private final AppConfig appConfig;


    @Autowired
    public GeneralController(UsuarioRepository usuarioRepository, AppConfig appConfig) {
        this.usuarioRepository = usuarioRepository;
        this.appConfig = appConfig;
    }

    /* Crie este método para a sua página inicial
    @GetMapping("/home")
    public String paginaInicial(Model model, Principal principal, AppConfig appConfig) {
        /*
        appConfig = AppConfig.getInstance();
        String appName = appConfig.getNomeAplicacao();
        String appVersion = appConfig.getVersao();


        if (principal != null) {
            String email = principal.getName();
            java.util.Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
            Usuario usuarioLogado = usuarioOptional.get();
            model.addAttribute("usuario", usuarioLogado);
            model.addAttribute("nomeAplicacao", appName);
            model.addAttribute("versionAplicacao", appVersion);
        }
        */
/*
        model.addAttribute("configuracaoApp", appConfig);
        return "home";

    }
    */

    @GetMapping("/home")
// MUDANÇA 1: Remova o AppConfig dos parâmetros do método.
    public String paginaInicial(Model model, Principal principal) {

        // Se um usuário estiver logado, vamos adicioná-lo ao model.
        if (principal != null) {
            String email = principal.getName();
            // findByEmail retorna um Optional. Se o usuário existir, nós o adicionamos.
            usuarioRepository.findByEmail(email).ifPresent(usuario -> {
                model.addAttribute("usuario", usuario);
            });
        }

        // MUDANÇA 2: Use o 'this.appConfig', que foi injetado pelo construtor.
        // Ele contém os valores corretos do application.properties.
        model.addAttribute("configuracaoApp", this.appConfig);

        return "home";
    }



    @GetMapping("/")
    public String redirect(){
        return "redirect:/home";
    }

    @GetMapping("/adm")
    public String adm(){
        return "adm-resources";
    }
/*
    @GetMapping("/estatisticas")
    public String estatisticas(){
        return "estatisticas";
    }
    */
    @GetMapping("/ping")
    public String ping(){
        return "ping";
    }




}

