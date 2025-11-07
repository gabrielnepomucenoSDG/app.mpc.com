package com.ongreport.narraterpro.repository;


import com.ongreport.narraterpro.entity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {
    // JpaRepository<TipoDaEntidade, TipoDaChavePrimaria>
    // Você pode adicionar consultas customizadas aqui no futuro, se precisar.
    Optional<Atividade> findByNome(String nome);
    List<Atividade> findByProjeto_IdProjeto(Integer projetoId);
}
