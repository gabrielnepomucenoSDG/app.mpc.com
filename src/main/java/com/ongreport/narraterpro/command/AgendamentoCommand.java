// src/main/java/com/ongreport/narraterpro/command/AgendamentoCommand.java
package com.ongreport.narraterpro.command;

// Importar Agendamento se os comandos forem operar sobre ele
import com.ongreport.narraterpro.entity.Agendamento;

public interface AgendamentoCommand {
    void execute();
    // Opcional: void undo(); // Para suportar a funcionalidade de "desfazer"
}