package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.importer.ImportResult;
import com.ongreport.narraterpro.service.imports.AgendamentoImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

/**
 * Padrão Facade: Ponto de entrada simplificado para todas as operações
 * relacionadas a agendamentos, orquestrando múltiplos serviços.
 */
@Service
public class AgendamentoFacade {

    private final AgendamentoService agendamentoService;
    private final AgendamentoImportService agendamentoImportService;
    // Futuramente, outros serviços podem ser injetados aqui.
    // private final NotificationService notificationService;
    // private final ReportService reportService;

    @Autowired
    public AgendamentoFacade(AgendamentoService agendamentoService, AgendamentoImportService agendamentoImportService) {
        this.agendamentoService = agendamentoService;
        this.agendamentoImportService = agendamentoImportService;
    }

    /**
     * Ponto de entrada para criar um agendamento individual (ex: via formulário).
     * Orquestra o salvamento e as ações de pós-criação.
     *
     * @param agendamento O objeto Agendamento a ser processado.
     * @return O agendamento salvo e processado.
     */
    @Transactional
    public Agendamento criarAgendamentoIndividual(Agendamento agendamento) {
        System.out.println("Facade: Iniciando processo de agendamento individual...");

        // 1. A responsabilidade primária de salvar é delegada ao serviço específico.
        Agendamento agendamentoSalvo = agendamentoService.salvar(agendamento);

        // 2. A Facade orquestra as ações secundárias.
        realizarAcoesPosCriacao(agendamentoSalvo);

        System.out.println("Facade: Processo de agendamento individual concluído.");
        return agendamentoSalvo;
    }

    /**
     * Ponto de entrada para importar agendamentos em massa.
     * Orquestra a importação e as ações de pós-criação.
     *
     * @param file O arquivo a ser importado.
     * @param principal O usuário que está realizando a importação.
     * @return Um resumo do resultado da importação.
     * @throws IOException Se houver erro de leitura.
     */
    @Transactional
    public ImportResult importarAgendamentos(MultipartFile file, Principal principal) throws IOException {
        System.out.println("Facade: Iniciando processo de importação em massa...");

        // 1. Delega a lógica de importação para o serviço especializado.
        ImportResult resultado = agendamentoImportService.importarArquivo(file, principal);

        // A lógica de pós-criação para itens importados pode ser diferente.
        // Por exemplo, você pode querer enviar um único e-mail de resumo em vez de um por agendamento.
        // if (resultado.getSuccessCount() > 0) {
        //     notificationService.enviarResumoImportacao(principal.getName(), resultado);
        // }

        System.out.println("Facade: Processo de importação em massa concluído.");
        return resultado;
    }

    /**
     * Método privado que centraliza as ações que devem ocorrer
     * sempre que um novo agendamento é criado individualmente.
     *
     * @param agendamento O agendamento que acabou de ser salvo.
     */
    private void realizarAcoesPosCriacao(Agendamento agendamento) {
        System.out.println("Facade: Realizando ações pós-criação para o Agendamento ID: " + agendamento.getIdAgendamento());

        // Exemplo 1: Enviar notificação por e-mail (futuramente)
        // notificationService.enviarConfirmacao(agendamento);

        // Exemplo 2: Atualizar um painel de relatórios (futuramente)
        // reportService.atualizarRelatorioAtividades(agendamento.getAtividade());
    }

    // Outros métodos da fachada podem ser mantidos
    public Agendamento buscarAgendamentoCompleto(Integer id) {
        return agendamentoService.buscarPorId(id);
    }
}
