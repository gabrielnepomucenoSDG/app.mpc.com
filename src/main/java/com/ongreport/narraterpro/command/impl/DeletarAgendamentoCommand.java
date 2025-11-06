// src/main/java/com/ongreport/narraterpro/command/impl/DeletarAgendamentoCommand.java
package com.ongreport.narraterpro.command.impl;

import com.ongreport.narraterpro.command.AgendamentoCommand;
import com.ongreport.narraterpro.service.AgendamentoService; // O Receiver

public class DeletarAgendamentoCommand implements AgendamentoCommand {

    private final AgendamentoService agendamentoService; // Receiver
    private final Integer agendamentoId; // Parâmetro para o comando
    // Opcional: Agendamento agendamentoAntigo; // Para armazenar o estado para 'undo'

    public DeletarAgendamentoCommand(AgendamentoService agendamentoService, Integer agendamentoId) {
        this.agendamentoService = agendamentoService;
        this.agendamentoId = agendamentoId;
    }

    @Override
    public void execute() {
        System.out.println("COMMAND: Executando DeletarAgendamentoCommand para ID: " + agendamentoId + "...");
        // Antes de deletar, se você fosse implementar undo, buscaria o agendamento
        // this.agendamentoAntigo = agendamentoService.buscarPorId(agendamentoId);

        agendamentoService.deletarLogicamente(agendamentoId); // Chama o método do Receiver
        System.out.println("COMMAND: Agendamento " + agendamentoId + " deletado logicamente.");
    }
    // Opcional: Lógica de undo para restaurar o agendamento
}