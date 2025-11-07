package com.ongreport.narraterpro.controller;

import com.ongreport.narraterpro.dto.stats.EstatisticasDTO;
import com.ongreport.narraterpro.repository.CidadeRepository;
import com.ongreport.narraterpro.service.EstatisticasService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/estatisticas")
public class EstatisticasController {

    private final EstatisticasService estatisticasService;
    private final CidadeRepository cidadeRepository;

    public EstatisticasController(EstatisticasService estatisticasService, CidadeRepository cidadeRepository) {
        this.estatisticasService = estatisticasService;
        this.cidadeRepository = cidadeRepository;
    }

    @GetMapping
    public String exibirEstatisticas(
            @RequestParam(value = "cidadeId", required = false, defaultValue = "1") Integer cidadeId,
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        // Define datas padrão se não forem fornecidas (ex: último mês)
        if (dataInicio == null || dataFim == null) {
            dataFim = LocalDate.now();
            dataInicio = dataFim.minusMonths(1);
        }

        EstatisticasDTO estatisticas = estatisticasService.getEstatisticasCompletas(cidadeId, dataInicio, dataFim);

        model.addAttribute("estatisticas", estatisticas);
        model.addAttribute("cidades", cidadeRepository.findAll());
        model.addAttribute("cidadeIdSelecionada", cidadeId);
        model.addAttribute("dataInicioSelecionada", dataInicio);
        model.addAttribute("dataFimSelecionada", dataFim);

        return "estatisticas-view";
    }
}
