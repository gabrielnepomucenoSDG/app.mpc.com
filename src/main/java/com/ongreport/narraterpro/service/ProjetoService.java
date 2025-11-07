package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.entity.Projeto;
import com.ongreport.narraterpro.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    @Autowired
    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    @Transactional(readOnly = true)
    public List<Projeto> findAll() {
        return projetoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Projeto> findById(Integer id) {
        return projetoRepository.findById(id);
    }

    @Transactional
    public Projeto save(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    @Transactional
    public void deleteById(Integer id) {
        projetoRepository.deleteById(id);
    }
}