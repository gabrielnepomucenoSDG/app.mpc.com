package com.ongreport.narraterpro.dto.stats;
//EXCLUIR
import java.math.BigDecimal;
import lombok.Data;
// A anotação @AllArgsConstructor deve ser removida ou não usada
// para garantir que nosso construtor customizado seja chamado pelo JPA.

@Data
public class ImpactoDTO {
    private long publicoTotal;
    private BigDecimal custoTotal;

    /**
     * CORREÇÃO FINAL: Este construtor agora corresponde exatamente aos tipos
     * retornados pela consulta JPQL (SUM(Integer) -> Long, SUM(BigDecimal) -> BigDecimal).
     *
     * @param publicoTotal O resultado de SUM(publico_estimado), que o JPA mapeia para Long.
     * @param custoTotal   O resultado de SUM(custos), que o JPA mapeia para BigDecimal.
     */
    public ImpactoDTO(Long publicoTotal, BigDecimal custoTotal) {
        // Se o resultado da soma for nulo (nenhum agendamento no período), assume 0.
        this.publicoTotal = (publicoTotal != null) ? publicoTotal : 0L;
        this.custoTotal = (custoTotal != null) ? custoTotal : BigDecimal.ZERO;
    }
}
