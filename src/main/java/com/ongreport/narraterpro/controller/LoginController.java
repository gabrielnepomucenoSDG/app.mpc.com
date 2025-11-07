package com.ongreport.narraterpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Este método responde às requisições GET para a URL "/login".
     * Ele simplesmente retorna o nome do template HTML que deve ser renderizado.
     * @return O nome do arquivo de template (sem o .html)
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // Isso vai procurar por um arquivo chamado "login.html" na pasta templates.
    }
}