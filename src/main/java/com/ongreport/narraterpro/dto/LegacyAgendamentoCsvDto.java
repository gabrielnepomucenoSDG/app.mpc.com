package com.ongreport.narraterpro.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data // Usa Lombok para gerar getters, setters, etc.
public class LegacyAgendamentoCsvDto {

    @CsvBindByName
    private String idLegado;

    @CsvBindByName
    private String data_inicio;

    @CsvBindByName
    private String turno;

    @CsvBindByName
    private String local;

    @CsvBindByName // No CSV legado, esta coluna contém o NOME da atividade
    private String atividade;

    @CsvBindByName
    private Integer publico_estimado;

    @CsvBindByName
    private String feedback_observ;
}