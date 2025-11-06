package com.ongreport.narraterpro.dto.stats;
//EXCLUIR
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstatisticasDTO {
    private List<RankingUsuarioDTO> ranking;
    private long atividadesPendentes;
    private ImpactoDTO impacto;
    private List<String> rankingLabels; // Para o gráfico
    private List<Integer> rankingData;  // Para o gráfico
}
