package com.ongreport.narraterpro.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityPerformanceDTO {
    private String nomeCidade;
    private Long quantidadeRegistros;
    private Double percentualFeedbacks;
    private Integer colocacao; // Para o ranking

    public CityPerformanceDTO(String nomeCidade, Long quantidadeRegistros, Double percentualFeedbacks) {
        this.nomeCidade = nomeCidade;
        this.quantidadeRegistros = quantidadeRegistros;
        this.percentualFeedbacks = percentualFeedbacks;
        // O campo 'colocacao' começará como nulo, para ser preenchido depois no Service.
    }
}