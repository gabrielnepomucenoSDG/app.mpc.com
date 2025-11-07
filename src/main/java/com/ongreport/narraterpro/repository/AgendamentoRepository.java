package com.ongreport.narraterpro.repository;

import com.ongreport.narraterpro.dto.stats.CityPerformanceDTO;
import com.ongreport.narraterpro.dto.stats.LatestAgendamentoDTO;
import com.ongreport.narraterpro.dto.stats.ProjectExecutionDTO;
import com.ongreport.narraterpro.entity.Agendamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    @Query("SELECT a FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0")
    List<Agendamento> findAllActive();


    // Query para o total de público alcançado
    @Query("SELECT SUM(a.publico_estimado) FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0")
    Long sumPublicoEstimado();

    // Query para o total de custos
    @Query("SELECT SUM(a.custos) FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0")
    BigDecimal sumCustos();

    // Query para contagem de agendamentos com feedback
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE (a.exclusao_logica IS NULL OR a.exclusao_logica = 0) AND (a.feedback_observ IS NOT NULL AND a.feedback_observ <> '')")
    Long countAgendamentosWithFeedback();

    // Query para contagem total de agendamentos
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0")
    Long countAllActiveAgendamentos();

    /*Query para os últimos registros para a tabela 1
    @Query("SELECT new com.ongreport.narraterpro.dto.stats.LatestAgendamentoDTO(a.atividade.nome, a.usuario.cidade.nome, a.data_inicio, (a.feedback_observ IS NOT NULL AND a.feedback_observ <> '')) " +
            "FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0 ORDER BY a.data_hora_criacao DESC")
    List<LatestAgendamentoDTO> findLatestAgendamentos(org.springframework.data.domain.Pageable pageable);
*/

    @Query("SELECT new com.ongreport.narraterpro.dto.stats.LatestAgendamentoDTO(a.atividade.nome, a.usuario.cidade.nome, a.data_inicio, (a.feedback_observ IS NOT NULL AND a.feedback_observ <> '')) " +
            "FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0")
    List<LatestAgendamentoDTO> findLatestAgendamentos(Pageable pageable);

    /* Query para os últimos registros para a tabela 1
    @Query("SELECT new com.ongreport.narraterpro.dto.stats.LatestAgendamentoDTO(a.atividade.nome, a.usuario.cidade.nome, a.data_inicio) " +
            "FROM Agendamento a WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0 ORDER BY a.data_hora_criacao DESC")
    List<LatestAgendamentoDTO> findLatestAgendamentos(org.springframework.data.domain.Pageable pageable);
    */


    // Query para projetos mais/menos executados
    @Query("SELECT new com.ongreport.narraterpro.dto.stats.ProjectExecutionDTO(p.nome, COUNT(a), AVG(a.custos)) " +
            "FROM Agendamento a JOIN a.atividade t JOIN t.projeto p " +
            "WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0 " +
            "GROUP BY p.nome ORDER BY COUNT(a) DESC")
    List<ProjectExecutionDTO> findProjectExecutionStats();

    // Query para ranking de desempenho por cidade
    @Query("SELECT new com.ongreport.narraterpro.dto.stats.CityPerformanceDTO(c.nome, COUNT(a), " +
            "SUM(CASE WHEN a.feedback_observ IS NOT NULL AND a.feedback_observ <> '' THEN 1.0 ELSE 0.0 END) * 100.0 / COUNT(a)) " +
            "FROM Agendamento a JOIN a.usuario u JOIN u.cidade c " +
            "WHERE a.exclusao_logica IS NULL OR a.exclusao_logica = 0 " +
            "GROUP BY c.nome ORDER BY COUNT(a) DESC, SUM(CASE WHEN a.feedback_observ IS NOT NULL AND a.feedback_observ <> '' THEN 1.0 ELSE 0.0 END) DESC")
    List<CityPerformanceDTO> findCityPerformanceRanking();
}
