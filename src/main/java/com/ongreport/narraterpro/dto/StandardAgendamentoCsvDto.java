package com.ongreport.narraterpro.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class StandardAgendamentoCsvDto {

    // ... outros campos do formato padrão ...
    @CsvBindByName
    private String data_inicio;

    @CsvBindByName
    private String data_fim;

    @CsvBindByName
    private String turno;

    @CsvBindByName
    private String local;

    @CsvBindByName
    private String custos;

    // CAMPO ADICIONADO POR VOCÊ
    @CsvBindByName
    private String atividade;

    // ... outros campos (favorito, publico_estimado, etc.)
    @CsvBindByName
    private String favorito;

    @CsvBindByName
    private Integer publico_estimado;

    @CsvBindByName
    private String recorrencia;

    @CsvBindByName
    private String feedback_observ;
}