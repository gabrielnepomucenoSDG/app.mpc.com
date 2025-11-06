package com.ongreport.narraterpro.observer;

import com.ongreport.narraterpro.entity.Agendamento;

public interface AgendamentoObserver {
    void update(Agendamento agendamento); // Recebe o agendamento atualizado
}