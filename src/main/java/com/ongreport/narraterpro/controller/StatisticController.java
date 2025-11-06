package com.ongreport.narraterpro.controller;

import com.ongreport.narraterpro.service.StatisticService;
import com.ongreport.narraterpro.config.AppConfig; // Para pegar nome e versão da aplicação
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.UsuarioRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal; // Para obter o usuário logado

@Controller
@RequestMapping("/estatisticas") // Mapeia para a URL /estatisticas
public class StatisticController {

    private final StatisticService statisticService;
    private final AppConfig appConfig; // Injete AppConfig
    private final UsuarioRepository usuarioRepository; // Para pegar o usuário logado e nome

    @Autowired
    public StatisticController(StatisticService statisticService, AppConfig appConfig, UsuarioRepository usuarioRepository) {
        this.statisticService = statisticService;
        this.appConfig = appConfig;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping // Responde a GET /estatisticas
    public String showStatistics(Model model, Principal principal) {
        // Dados para os cards de estatísticas
        model.addAttribute("totalPessoasAlcancadas", statisticService.getTotalPessoasAlcancadas());
        model.addAttribute("totalInvestido", statisticService.getTotalInvestidoFormatado());
        model.addAttribute("percentualFeedback", statisticService.getPercentualFeedback());

        // Dados para a tabela de últimos registros (ex: 5 últimos)
        model.addAttribute("ultimosRegistros", statisticService.getLatestAgendamentos(5));

        // Dados para a tabela de projetos
        model.addAttribute("projetosExecucao", statisticService.getProjectExecutionStats());

        // Dados para a tabela de ranking de cidades
        model.addAttribute("rankingCidades", statisticService.getCityPerformanceWithRanking());

        /* Adiciona informações da aplicação (do AppConfig)
        model.addAttribute("nomeAplicacao", appConfig.getNomeAplicacao());
        model.addAttribute("versionAplicacao", appConfig.getVersao());*/

        // Adiciona informações do usuário logado (para a navbar, se for um fragmento)
        if (principal != null) {
            String email = principal.getName();
            usuarioRepository.findByEmail(email).ifPresent(usuarioLogado ->
                    model.addAttribute("usuarioLogado", usuarioLogado) // Use um nome diferente para evitar conflito com 'usuario' de outras telas
            );
        }

        return "estatisticas"; // Nome do seu template HTML
    }
}