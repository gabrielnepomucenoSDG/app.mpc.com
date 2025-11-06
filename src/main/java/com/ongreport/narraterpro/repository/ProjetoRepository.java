package com.ongreport.narraterpro.repository;


import com.ongreport.narraterpro.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
    // JpaRepository<TipoDaEntidade, TipoDaChavePrimaria>
    // Você pode adicionar consultas customizadas aqui no futuro, se precisar.
}
