package com.ongreport.narraterpro.repository;


import com.ongreport.narraterpro.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
    // JpaRepository<TipoDaEntidade, TipoDaChavePrimaria>
    // Você pode adicionar consultas customizadas aqui no futuro, se precisar.
}
