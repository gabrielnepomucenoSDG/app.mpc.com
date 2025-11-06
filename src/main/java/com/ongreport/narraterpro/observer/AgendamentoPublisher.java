package com.ongreport.narraterpro.observer;

import com.ongreport.narraterpro.entity.Agendamento; // Para passar o objeto de estado

public interface AgendamentoPublisher {
    void addObserver(AgendamentoObserver observer);
    void removeObserver(AgendamentoObserver observer);
    void notifyObservers(Agendamento agendamento); // Notifica com o objeto Agendamento alterado
}