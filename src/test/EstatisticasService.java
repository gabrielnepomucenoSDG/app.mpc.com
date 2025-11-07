package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.dto.stats.EstatisticasDTO;
import com.ongreport.narraterpro.dto.stats.ImpactoDTO;
import com.ongreport.narraterpro.dto.stats.RankingUsuarioDTO;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.AgendamentoRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class EstatisticasService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public EstatisticasService(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EstatisticasDTO getEstatisticasCompletas(Integer cidadeId, LocalDate dataInicio, LocalDate dataFim) {

        // Ranking de Usuários
        List<Usuario> rankingUsuarios = usuarioRepository.findTop10ByCidade_IdOrderByPontuacaoDesc(cidadeId);
        List<RankingUsuarioDTO> rankingDto = rankingUsuarios.stream()
                .map(u -> new RankingUsuarioDTO(u.getNome(), u.getFuncao(), u.getPontuacao()))
                .collect(Collectors.toList());

        // Contagem de Atividades Pendentes
        long pendentes = agendamentoRepository.countAgendamentosPendentes();

        // Cálculo de Impacto
        ImpactoDTO impacto = agendamentoRepository.getImpactoPorPeriodo(dataInicio, dataFim);

        // Preparar dados para o gráfico
        List<String> labels = rankingDto.stream().map(RankingUsuarioDTO::getNome).collect(Collectors.toList());
        List<Integer> data = rankingDto.stream().map(RankingUsuarioDTO::getPontuacao).collect(Collectors.toList());

        return EstatisticasDTO.builder()
                .ranking(rankingDto)
                .atividadesPendentes(pendentes)
                .impacto(impacto)
                .rankingLabels(labels)
                .rankingData(data)
                .build();
    }
}
