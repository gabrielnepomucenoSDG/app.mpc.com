package com.ongreport.narraterpro.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectExecutionDTO {
    private String nomeProjeto;
    private Long execucoes; // Usar Long para contagens
    private Double custoMedio;
}