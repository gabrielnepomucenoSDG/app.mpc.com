package com.ongreport.narraterpro.dto.stats;

import com.ongreport.narraterpro.entity.Agendamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestAgendamentoDTO {
    private String atividadeNome;
    private String cidadeNome;
    private LocalDate dataInicio;
    private boolean hasFeedback;

}