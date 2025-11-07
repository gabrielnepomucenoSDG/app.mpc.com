package com.ongreport.narraterpro.dto.estatisticas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingUsuarioDTO {
    private String nome;
    private String funcao;
    private Integer pontuacao;
}
