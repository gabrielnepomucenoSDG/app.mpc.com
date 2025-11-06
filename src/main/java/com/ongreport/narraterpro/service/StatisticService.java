package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.repository.AgendamentoRepository;
import com.ongreport.narraterpro.dto.stats.LatestAgendamentoDTO;
import com.ongreport.narraterpro.dto.stats.ProjectExecutionDTO;
import com.ongreport.narraterpro.dto.stats.CityPerformanceDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final AgendamentoRepository agendamentoRepository;

    @Autowired
    public StatisticService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Transactional(readOnly = true)
    public Long getTotalPessoasAlcancadas() {
        return agendamentoRepository.sumPublicoEstimado();
    }

    @Transactional(readOnly = true)
    public String getTotalInvestidoFormatado() {
        BigDecimal totalCustos = agendamentoRepository.sumCustos();
        if (totalCustos == null) {
            totalCustos = BigDecimal.ZERO;
        }
        // Formata como moeda brasileira
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormat.format(totalCustos);
    }

    @Transactional(readOnly = true)
    public String getPercentualFeedback() {
        Long totalWithFeedback = agendamentoRepository.countAgendamentosWithFeedback();
        Long totalAgendamentos = agendamentoRepository.countAllActiveAgendamentos();

        if (totalAgendamentos == null || totalAgendamentos == 0) {
            return "0%";
        }

        double percentage = (double) totalWithFeedback / totalAgendamentos * 100;
        return String.format(Locale.US, "%.0f%%", percentage); // Formata para 0 casas decimais e adiciona '%'
    }
/*
    @Transactional(readOnly = true)
    public List<LatestAgendamentoDTO> getLatestAgendamentos(int limit) {
        // Usa PageRequest para limitar o número de resultados
        return agendamentoRepository.findLatestAgendamentos(PageRequest.of(0, limit));
    }
    */
public List<LatestAgendamentoDTO> getLatestAgendamentos(int limit) {
    // 1. Defina o número da página e o tamanho.
    // Para os "últimos" agendamentos, a página será sempre a primeira (0).
    int page = 0;
    int size = limit; // ex: 5, 10, etc.

    // 2. Crie as instruções de ordenação que queremos.
    // Ordenar pelo campo "dataHoraCriacao" em ordem descendente.
    Sort sort = Sort.by(Sort.Direction.DESC, "data_hora_criacao");

    // 3. Crie o objeto Pageable com a paginação E a ordenação.
    Pageable pageable = PageRequest.of(page, size, sort);

    // 4. Chame o método do repositório com o Pageable configurado corretamente.
    return agendamentoRepository.findLatestAgendamentos(pageable);
}

    @Transactional(readOnly = true)
    public List<ProjectExecutionDTO> getProjectExecutionStats() {
        return agendamentoRepository.findProjectExecutionStats();
    }

    @Transactional(readOnly = true)
    public List<CityPerformanceDTO> getCityPerformanceWithRanking() {

        // 1. Chama o método do repositório, que retorna a lista sem a colocação.
        // A lista já virá ordenada do banco de dados, como definimos no ORDER BY.
        List<CityPerformanceDTO> rankingData = agendamentoRepository.findCityPerformanceRanking();

        // 2. Itera sobre a lista para preencher a colocação
        for (int i = 0; i < rankingData.size(); i++) {
            // Pega o DTO na posição 'i' e define a colocação como 'i + 1'
            rankingData.get(i).setColocacao(i + 1);
        }

        // 3. Retorna a lista completa, agora com a colocação preenchida.
        return rankingData;
    }
}