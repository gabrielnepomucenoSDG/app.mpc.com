// src/main/java/com/ongreport/narraterpro/command/impl/SalvarAgendamentoCommand.java
package com.ongreport.narraterpro.command.impl;

import com.ongreport.narraterpro.command.AgendamentoCommand;
import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.service.AgendamentoService; // O Receiver

public class SalvarAgendamentoCommand implements AgendamentoCommand {

    private final AgendamentoService agendamentoService; // Receiver
    private final Agendamento agendamento; // Parâmetro para o comando

    public SalvarAgendamentoCommand(AgendamentoService agendamentoService, Agendamento agendamento) {
        this.agendamentoService = agendamentoService;
        this.agendamento = agendamento;
    }

    @Override
    public void execute() {
        System.out.println("COMMAND: Executando SalvarAgendamentoCommand...");
        // A lógica de salvar será executada pelo Receiver
        agendamentoService.salvar(agendamento);
        System.out.println("COMMAND: Agendamento salvo/atualizado com ID: " + agendamento.getIdAgendamento());
    }

    // Você pode adicionar a lógica de 'undo' aqui se precisar de histórico/desfazer
    // Ex: Se fosse uma criação, o undo seria deletar. Se fosse uma edição, reverter para o estado anterior.
    // Para simplificar, vou omitir o undo por agora, mas saiba que é aqui que ele iria.
}