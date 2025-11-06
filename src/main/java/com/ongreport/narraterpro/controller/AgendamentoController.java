package com.ongreport.narraterpro.controller;

import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.entity.Atividade;
import com.ongreport.narraterpro.entity.Projeto;
import com.ongreport.narraterpro.repository.AtividadeRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import com.ongreport.narraterpro.service.AgendamentoService;
import com.ongreport.narraterpro.service.AgendamentoFacade;

// Imports para o padrão Command
import com.ongreport.narraterpro.command.AgendamentoCommand;
import com.ongreport.narraterpro.command.impl.SalvarAgendamentoCommand;
import com.ongreport.narraterpro.command.impl.DeletarAgendamentoCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/agendamentos") // Todas as URLs deste controller começarão com /agendamentos
public class AgendamentoController {

    private final AgendamentoService agendamentoService; // Atua como Receiver para Commands e Publisher para Observers
    private final AgendamentoFacade agendamentoFacade; // Facade para operações de alto nível
    private final UsuarioRepository usuarioRepository; // Usado para buscar o usuário logado
    private final AtividadeRepository atividadeRepository;


    @Autowired
    public AgendamentoController(AgendamentoService agendamentoService,
                                 AgendamentoFacade agendamentoFacade,
                                 UsuarioRepository usuarioRepository, AtividadeRepository atividadeRepository) {
        this.agendamentoService = agendamentoService;
        this.agendamentoFacade = agendamentoFacade;
        this.usuarioRepository = usuarioRepository;
        this.atividadeRepository = atividadeRepository;
    }

    // Método para EXIBIR o formulário de novo agendamento
    @GetMapping("/novo")
    public String exibirFormularioDeAgendamento(Model model, Principal principal) {
        // Busca os dados necessários para popular os dropdowns do formulário
        List<Projeto> projetos = agendamentoService.buscarTodosProjetos();
        List<Atividade> atividades = agendamentoService.buscarTodasAtividades();

        // Prepara um novo Agendamento já associado ao usuário logado
        Agendamento agendamento = agendamentoService.prepararNovoAgendamento(principal.getName());

        model.addAttribute("agendamento", agendamento); // Objeto para vincular ao form
        model.addAttribute("todosProjetos", projetos);
        model.addAttribute("todasAtividades", atividades);

        // Retorna o nome do arquivo HTML que deve ser renderizado
        return "agendamento-form";
    }

    // Método para EXIBIR o formulário de feedback (reutiliza o form de agendamento)
    @GetMapping("/feedback/{id}")
    public String exibirFormularioDeFeedback(@PathVariable Integer id, Model model) {
        // Busca o agendamento existente no banco
        Agendamento agendamento = agendamentoService.buscarPorId(id);

        // Adiciona todos os dados necessários ao model
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("todosProjetos", agendamentoService.buscarTodosProjetos());
        model.addAttribute("todasAtividades", agendamentoService.buscarTodasAtividades());

        // Reutiliza o mesmo formulário!
        return "agendamento-form";
    }

    // Método para PROCESSAR os dados do formulário de agendamento/feedback
    @PostMapping("/salvar")
    public String salvarAgendamento(@ModelAttribute("agendamento") Agendamento agendamento, RedirectAttributes redirectAttributes) {
        try {
            // Aplicação do Padrão Command: Encapsula a ação de salvar
            // O AgendamentoService atua como o Receiver do comando
            AgendamentoCommand salvarCommand = new SalvarAgendamentoCommand(agendamentoService, agendamento);
            salvarCommand.execute(); // O Controller (Invoker) executa o comando

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Agendamento salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar agendamento: " + e.getMessage());
            // Redireciona para o formulário apropriado em caso de erro
            if (agendamento.getIdAgendamento() != null) {
                return "redirect:/agendamentos/feedback/" + agendamento.getIdAgendamento();
            } else {
                return "redirect:/agendamentos/novo";
            }
        }
        return "redirect:/agendamentos"; // Redireciona para a lista de agendamentos
    }

    // Método para listar todos os agendamentos ativos
    @GetMapping
    public String listarAgendamentos(Model model) {
        List<Agendamento> agendamentos = agendamentoService.buscarTodos();
        model.addAttribute("agendamentos", agendamentos);
        return "agendamentos-lista";
    }


    @PostMapping("/favoritar/{id}")
    public String toggleFavorito(@PathVariable("id") Integer id) {
        // 1. Busca o agendamento no banco de dados
        Agendamento agendamento = agendamentoService.buscarPorId(id);

        // 2. Verifica se o agendamento foi encontrado
        if (agendamento != null) {
            // 3. Inverte o valor do campo 'favorito'
            // Se for 1, vira 0. Se for 0 (ou nulo), vira 1.
            if (agendamento.getFavorito() != null && agendamento.getFavorito() == 1) {
                agendamento.setFavorito(0);
            } else {
                agendamento.setFavorito(1);
            }

            // 4. Salva a alteração no banco
            agendamentoService.salvar(agendamento);
        }

        // 5. Redireciona o usuário de volta para a lista de agendamentos
        return "redirect:/agendamentos";
    }

    // Método para deletar logicamente um agendamento
    @GetMapping("/deletar/{id}")
    public String deletarAgendamento(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Aplicação do Padrão Command: Encapsula a ação de deletar
            AgendamentoCommand deletarCommand = new DeletarAgendamentoCommand(agendamentoService, id);
            deletarCommand.execute(); // O Controller (Invoker) executa o comando

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Agendamento excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir agendamento: " + e.getMessage());
        }
        return "redirect:/agendamentos";
    }

    // Método para exibir o formulário de edição de agendamento
    @GetMapping("/editar/{id}")
    public String exibirFormularioDeEdicao(@PathVariable Integer id, Model model) {
        Agendamento agendamento = agendamentoService.buscarPorId(id);
        if (agendamento == null) {
            return "redirect:/agendamentos"; // Redireciona se o agendamento não for encontrado
        }
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("todosProjetos", agendamentoService.buscarTodosProjetos());
        model.addAttribute("todasAtividades", agendamentoService.buscarTodasAtividades());
        return "agendamento-form";
    }

    @GetMapping("/atividadesPorProjeto")
    @ResponseBody // <-- ESSA ANOTAÇÃO É CRUCIAL!
    public List<Atividade> getAtividadesPorProjeto(@RequestParam("projetoId") Integer projetoId) {
        // A anotação @ResponseBody diz ao Spring para retornar os dados diretamente
        // como JSON, em vez de procurar por um template HTML.

        if (projetoId == null) {
            return Collections.emptyList(); // Retorna lista vazia se nenhum projeto for selecionado
        }
        // Usa o novo método que criamos no repositório.
        // O nome correto da classe é AtividadeRepository.
        return atividadeRepository.findByProjeto_IdProjeto(projetoId);
    }
}
