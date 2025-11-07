package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.dto.stats.CityPerformanceDTO;
import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.entity.Atividade;
import com.ongreport.narraterpro.entity.Projeto;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.observer.AgendamentoObserver;
import com.ongreport.narraterpro.observer.AgendamentoPublisher;
import com.ongreport.narraterpro.repository.AgendamentoRepository;
import com.ongreport.narraterpro.repository.AtividadeRepository;
import com.ongreport.narraterpro.repository.ProjetoRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService implements AgendamentoPublisher {


    // Injetando os repositórios que este serviço vai precisar usar.
    // O Spring vai fornecer as instâncias automaticamente (Injeção de Dependência).
  private AgendamentoRepository agendamentoRepository;
  private AtividadeRepository atividadeRepository;
  private ProjetoRepository projetoRepository;
  private UsuarioRepository usuarioRepository;

    private List<AgendamentoObserver> observers = new ArrayList<>();

    @Autowired // Garanta que todas as dependências do construtor estejam aqui
    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              AtividadeRepository atividadeRepository,
                              ProjetoRepository projetoRepository,
                              UsuarioRepository usuarioRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.atividadeRepository = atividadeRepository;
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Implementação dos métodos do AgendamentoPublisher
    @Override
    public void addObserver(AgendamentoObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AgendamentoObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Agendamento agendamento) {
        for (AgendamentoObserver observer : observers) {
            observer.update(agendamento);
        }
    }
    /**
     * Prepara um novo objeto Agendamento, já associando o usuário logado a ele.
      * @param email O nome de usuário (login) do usuário atualmente autenticado.
      * @return Um objeto Agendamento pronto para ser usado no formulário.
     */
    public Agendamento prepararNovoAgendamento(String email) {
        // Busca o usuário completo pelo nome de usuário
        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário '" + email + "' não encontrado."));

        // Cria a nova instância de Agendamento
        Agendamento agendamento = new Agendamento();

        // Associa o usuário encontrado ao agendamento
        agendamento.setUsuario(usuarioLogado);

        return agendamento;
    }

    // Método para buscar todos os agendamentos (será útil para uma tela de listagem)
    public List<Agendamento> buscarTodos() {

        return agendamentoRepository.findAllActive();
    }

    /* Método para salvar um novo agendamento ou atualizar um existente
    public Agendamento salvar(Agendamento agendamento) {
        // Aqui você poderia adicionar lógicas complexas antes de salvar.
        // Por exemplo: verificar se a data é válida, se o usuário tem permissão, etc.
        // Por enquanto, vamos apenas salvar.
        return agendamentoRepository.save(agendamento);
    }*/
    public Agendamento salvar(Agendamento agendamento) {

        // VERIFICAÇÃO: Se o ID é nulo, significa que é um novo agendamento.
        if (agendamento.getIdAgendamento() == null) {
            // Define a data e hora de criação para o momento exato desta chamada.
            agendamento.setData_hora_criacao(LocalDateTime.now());
        }

        // O resto da sua lógica de salvar, que pode ter outras regras no futuro,
        // continua aqui. Por fim, salvamos no repositório.
        return agendamentoRepository.save(agendamento);
    }


    // Métodos para popular os formulários (dropdowns)
    public List<Projeto> buscarTodosProjetos() {
        return projetoRepository.findAll();
    }

    public List<Atividade> buscarTodasAtividades() {
        return atividadeRepository.findAll();
    }

    // Você pode criar um método para buscar um agendamento por ID para a tela de feedback
    public Agendamento buscarPorId(Integer id) {
        // .orElse(null) é uma forma simples de lidar com o Optional retornado pelo findById
        return agendamentoRepository.findById(id).orElse(null);
    }

    public void deletarLogicamente(Integer id) {
        // Busca o agendamento no banco. Se não encontrar, lança uma exceção.
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        // Marca como excluído
        agendamento.setExclusao_logica(1);

        // Salva a alteração. O JPA fará um UPDATE.
        agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> buscarPorUsuarioLogado(String username) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));

        // 2. Usa o método do repositório que já definimos ('findByUsuario')
        // (Este método está no seu 'AgendamentoRepository.java (Trecho)')
        return agendamentoRepository.findByUsuario(usuarioLogado);
    }

}