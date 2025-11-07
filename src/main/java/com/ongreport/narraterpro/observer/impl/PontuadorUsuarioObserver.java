package com.ongreport.narraterpro.observer.impl;

import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.observer.AgendamentoObserver;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PontuadorUsuarioObserver implements AgendamentoObserver {

    @Autowired
    private UsuarioRepository usuarioRepository; // Para atualizar a pontuação do usuário

    @Override
    public void update(Agendamento agendamento) {
        // Lógica para pontuar o usuário com base no agendamento
        if (agendamento.getFeedback_observ() != null && !agendamento.getFeedback_observ().isEmpty()) {
            Usuario usuario = agendamento.getUsuario();
            if (usuario != null) {
                Integer pontuacaoAtual = usuario.getPontuacao() != null ? usuario.getPontuacao() : 0;
                usuario.setPontuacao(pontuacaoAtual + 10); // Exemplo: Adiciona 10 pontos
                usuarioRepository.save(usuario);
                System.out.println("OBSERVER: PontuadorUsuarioObserver - Usuário " + usuario.getEmail() + " pontuado. Nova pontuação: " + usuario.getPontuacao());
            }
        }
    }
}