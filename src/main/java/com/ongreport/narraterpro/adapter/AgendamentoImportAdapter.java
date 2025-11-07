package com.ongreport.narraterpro.adapter;

import com.ongreport.narraterpro.dto.LegacyAgendamentoCsvDto;
import com.ongreport.narraterpro.dto.StandardAgendamentoCsvDto;
import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.entity.Atividade;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AgendamentoImportAdapter {

    private final AtividadeRepository atividadeRepository;

    @Autowired
    public AgendamentoImportAdapter(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    // O método fromLegacyDto não precisa de alterações.
    public Agendamento fromLegacyDto(LegacyAgendamentoCsvDto dto, Usuario usuarioLogado) {
        // ... (código existente)
        if (dto.getAtividade() == null || dto.getAtividade().trim().isEmpty()) {
            throw new IllegalArgumentException("A coluna 'atividade' está vazia para o registro com idLegado: " + dto.getIdLegado());
        }
        Atividade atividade = atividadeRepository.findByNome(dto.getAtividade().trim())
                .orElseThrow(() -> new RuntimeException("Atividade '" + dto.getAtividade() + "' não foi encontrada no banco de dados."));
        Agendamento agendamento = new Agendamento();
        agendamento.setData_inicio(LocalDate.parse(dto.getData_inicio()));
        agendamento.setTurno(dto.getTurno());
        agendamento.setLocal(dto.getLocal());
        agendamento.setPublico_estimado(dto.getPublico_estimado());
        agendamento.setFeedback_observ(dto.getFeedback_observ());
        agendamento.setUsuario(usuarioLogado);
        agendamento.setAtividade(atividade);
        agendamento.setData_hora_criacao(LocalDateTime.now());
        agendamento.setExclusao_logica(0);
        return agendamento;
    }

    public Agendamento fromStandardDto(StandardAgendamentoCsvDto dto, Usuario usuarioLogado) {
        if (dto.getAtividade() == null || dto.getAtividade().trim().isEmpty()) {
            throw new IllegalArgumentException("A coluna 'atividade' está vazia para o registro com data de início: " + dto.getData_inicio());
        }

        Atividade atividade = atividadeRepository.findByNome(dto.getAtividade().trim())
                .orElseThrow(() -> new RuntimeException("Atividade '" + dto.getAtividade() + "' não foi encontrada no banco de dados."));

        Agendamento agendamento = new Agendamento();

        // Conversões de data
        if (dto.getData_inicio() != null && !dto.getData_inicio().isEmpty()) {
            agendamento.setData_inicio(LocalDate.parse(dto.getData_inicio()));
        }
        if (dto.getData_fim() != null && !dto.getData_fim().isEmpty()) {
            agendamento.setData_fim(LocalDate.parse(dto.getData_fim()));
        }

        agendamento.setPublico_estimado(dto.getPublico_estimado());
        agendamento.setFeedback_observ(dto.getFeedback_observ());
        agendamento.setTurno(dto.getTurno());
        agendamento.setLocal(dto.getLocal());

        // --- LÓGICA DE TRADUÇÃO INTELIGENTE ---

        // 1. Converte 'favorito'
        if (dto.getFavorito() != null && "sim".equalsIgnoreCase(dto.getFavorito().trim())) {
            agendamento.setFavorito(1);
        } else {
            agendamento.setFavorito(0);
        }

        // 2. Converte 'recorrencia' (mapeando texto para dias)
        if (dto.getRecorrencia() != null) {
            switch (dto.getRecorrencia().trim().toLowerCase()) {
                case "semanal": agendamento.setRecorrencia(7); break;
                case "quinzenal": agendamento.setRecorrencia(15); break;
                case "mensal": agendamento.setRecorrencia(30); break;
                case "bimestral": agendamento.setRecorrencia(60); break;
                case "semestral": agendamento.setRecorrencia(180); break;
                default: agendamento.setRecorrencia(0); break;
            }
        } else {
            agendamento.setRecorrencia(0);
        }

        // 3. Converte 'custos' (removendo "R$" e espaços)
        if (dto.getCustos() != null && !dto.getCustos().isBlank()) {
            String valorLimpo = dto.getCustos()
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", "")
                    .trim();
            agendamento.setCustos(new BigDecimal(valorLimpo));
        }



        agendamento.setUsuario(usuarioLogado);
        agendamento.setAtividade(atividade);
        agendamento.setData_hora_criacao(LocalDateTime.now());
        agendamento.setExclusao_logica(0);

        return agendamento;
    }
}
