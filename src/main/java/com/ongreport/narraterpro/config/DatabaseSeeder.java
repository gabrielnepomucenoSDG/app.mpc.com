package com.ongreport.narraterpro.config; // (Usei o pacote que vi na sua entidade)

// Importe TODOS os seus modelos e repositórios
import com.ongreport.narraterpro.entity.Cidade;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.entity.Projeto;
import com.ongreport.narraterpro.entity.Atividade;
import com.ongreport.narraterpro.entity.Agendamento;
import com.ongreport.narraterpro.repository.CidadeRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import com.ongreport.narraterpro.repository.ProjetoRepository;
import com.ongreport.narraterpro.repository.AtividadeRepository;
import com.ongreport.narraterpro.repository.AgendamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("postgres") // Só rode este componente se o perfil for "postgres"
public class DatabaseSeeder implements CommandLineRunner {

    // Injete TODOS os seus repositórios
    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private AtividadeRepository atividadeRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProjetoRepository projetoRepository;
    @Autowired private CidadeRepository cidadeRepository;


    @Override
    public void run(String... args) throws Exception {

        System.out.println(">>> [Ragui Guide] Rodando o DatabaseSeeder CORRIGIDO para o perfil 'postgres'...");

        // -------------------------------------------------------------------
        // FASE 1: LIMPEZA (WIPE)
        // -------------------------------------------------------------------
        System.out.println(">>> [Ragui Guide] Limpando dados antigos (Wipe)...");
        agendamentoRepository.deleteAll();
        // Verifique se Usuario depende de Cidade (sim, FK Cidade_idCidade)
        if (usuarioRepository != null) { // Adicionando verificações nulas para segurança
            usuarioRepository.deleteAll();
        }
        atividadeRepository.deleteAll();  // (Depende de Projeto)
        projetoRepository.deleteAll();
        cidadeRepository.deleteAll();

        System.out.println(">>> [Ragui Guide] Dados limpos.");

        // -------------------------------------------------------------------
        // FASE 2: POPULAÇÃO (SEED)
        // -------------------------------------------------------------------

        // ---- 1. Populando Cidades (Pais) ----
        System.out.println(">>> [Ragui Guide] Populando Cidades...");
        List<Cidade> cidades = new ArrayList<>();

        Cidade c1 = new Cidade(); c1.setNome("São Luís"); c1.setEstado("MA"); cidades.add(c1);
        Cidade c2 = new Cidade(); c2.setNome("Teresina"); c2.setEstado("PI"); cidades.add(c2);
        Cidade c3 = new Cidade(); c3.setNome("Aurora"); c3.setEstado("CE"); cidades.add(c3);
        Cidade c4 = new Cidade(); c4.setNome("Belo Horizonte"); c4.setEstado("MG"); cidades.add(c4);
        Cidade c5 = new Cidade(); c5.setNome("Anápolis"); c5.setEstado("GO"); cidades.add(c5);
        Cidade c6 = new Cidade(); c6.setNome("Goiás"); c6.setEstado("GO"); cidades.add(c6);
        Cidade c7 = new Cidade(); c7.setNome("Santa Maria"); c7.setEstado("RS"); cidades.add(c7);
        Cidade c8 = new Cidade(); c8.setNome("Campo Grande"); c8.setEstado("MS"); cidades.add(c8);

        cidadeRepository.saveAll(cidades);

        // ---- 2. Populando Projetos e Atividades (Pais e Filhos) ----
        System.out.println(">>> [Ragui Guide] Populando Projetos e Atividades...");

        List<Atividade> atividadesParaSalvar = new ArrayList<>();

        // Categoria: Nacionais
        Projeto acampamento = new Projeto();
        acampamento.setCategoria("Nacionais"); acampamento.setNome("ACAMPAMENTO");
        projetoRepository.save(acampamento); // Salva o pai
        // Passamos o objeto 'acampamento' salvo para criar os filhos
        atividadesParaSalvar.add(createAtividade("Eventos", acampamento));
        atividadesParaSalvar.add(createAtividade("Som do Céu", acampamento));
        atividadesParaSalvar.add(createAtividade("Temporada", acampamento));

        Projeto conexoes = new Projeto();
        conexoes.setCategoria("Nacionais"); conexoes.setNome("CONEXÕES");
        projetoRepository.save(conexoes);
        atividadesParaSalvar.add(createAtividade("Eventos", conexoes));
        atividadesParaSalvar.add(createAtividade("Relatórios", conexoes));

        Projeto cursos = new Projeto();
        cursos.setCategoria("Nacionais"); cursos.setNome("CURSOS E TREINAMENTOS");
        projetoRepository.save(cursos);
        atividadesParaSalvar.add(createAtividade("Ead de Capelania 200h", cursos));
        atividadesParaSalvar.add(createAtividade("Ead de Liderança", cursos));
        atividadesParaSalvar.add(createAtividade("Escola de Jesus", cursos));
        atividadesParaSalvar.add(createAtividade("Treinamento de Líderes", cursos));

        Projeto debora = new Projeto();
        debora.setCategoria("Nacionais"); debora.setNome("DESPERTA DÉBORA");
        projetoRepository.save(debora);
        atividadesParaSalvar.add(createAtividade("Congresso", debora));
        atividadesParaSalvar.add(createAtividade("Implantação", debora));
        atividadesParaSalvar.add(createAtividade("Renovo", debora));

        Projeto online = new Projeto();
        online.setCategoria("Nacionais"); online.setNome("ONLINE");
        projetoRepository.save(online);
        atividadesParaSalvar.add(createAtividade("Outros", online));
        atividadesParaSalvar.add(createAtividade("Podcast", online));
        atividadesParaSalvar.add(createAtividade("Rádio MPC", online));

        Projeto oracao = new Projeto();
        oracao.setCategoria("Nacionais"); oracao.setNome("ORAÇÃO");
        projetoRepository.save(oracao);
        atividadesParaSalvar.add(createAtividade("Intensivão", oracao));
        atividadesParaSalvar.add(createAtividade("Reunião", oracao));

        Projeto planejamentoNac = new Projeto();
        planejamentoNac.setCategoria("Nacionais"); planejamentoNac.setNome("PLANEJAMENTO");
        projetoRepository.save(planejamentoNac);
        atividadesParaSalvar.add(createAtividade("Evento", planejamentoNac));
        atividadesParaSalvar.add(createAtividade("Congresso", planejamentoNac));
        atividadesParaSalvar.add(createAtividade("Implantações", planejamentoNac));

        Projeto especiais = new Projeto();
        especiais.setCategoria("Nacionais"); especiais.setNome("PROJETOS ESPECIAIS");
        projetoRepository.save(especiais);
        atividadesParaSalvar.add(createAtividade("Projeto Boas Ondas", especiais));
        atividadesParaSalvar.add(createAtividade("Projeto Amar", especiais));
        atividadesParaSalvar.add(createAtividade("Projeto Visão", especiais));

        // Categoria: Locais
        Projeto internas = new Projeto();
        internas.setCategoria("Locais"); internas.setNome("ATIVIDADES INTERNAS E PLANEJAMENTO");
        projetoRepository.save(internas);
        atividadesParaSalvar.add(createAtividade("mobilização de fundos", internas));
        atividadesParaSalvar.add(createAtividade("mobilização de voluntários", internas));
        atividadesParaSalvar.add(createAtividade("reunião com pastores e líderes", internas));
        atividadesParaSalvar.add(createAtividade("reunião de oração", internas));

        Projeto capelania = new Projeto();
        capelania.setCategoria("Locais"); capelania.setNome("CAPELANIA");
        projetoRepository.save(capelania);
        atividadesParaSalvar.add(createAtividade("A Grande Festa", capelania));
        atividadesParaSalvar.add(createAtividade("Capaz", capelania));
        atividadesParaSalvar.add(createAtividade("Catavento", capelania));

        Projeto crossplay = new Projeto();
        crossplay.setCategoria("Locais"); crossplay.setNome("CROSSPLAY");
        projetoRepository.save(crossplay);
        atividadesParaSalvar.add(createAtividade("Campeonato", crossplay));
        atividadesParaSalvar.add(createAtividade("Discipulado", crossplay));
        atividadesParaSalvar.add(createAtividade("Evangelismo", crossplay));

        Projeto escolaVida = new Projeto();
        escolaVida.setCategoria("Locais"); escolaVida.setNome("ESCOLA DA VIDA");
        projetoRepository.save(escolaVida);
        atividadesParaSalvar.add(createAtividade("Alunos - Apresentação do Amor", escolaVida));
        atividadesParaSalvar.add(createAtividade("Alunos - Concurso de Redação", escolaVida));
        atividadesParaSalvar.add(createAtividade("Alunos - Palestra", escolaVida));
        atividadesParaSalvar.add(createAtividade("Professores e colaboradores - Lanche", escolaVida));

        Projeto estudantes = new Projeto();
        estudantes.setCategoria("Locais"); estudantes.setNome("ESTUDANTES EM AÇÃO");
        projetoRepository.save(estudantes);
        atividadesParaSalvar.add(createAtividade("Adote sua Sala", estudantes));
        atividadesParaSalvar.add(createAtividade("Jovem Alvo", estudantes));
        atividadesParaSalvar.add(createAtividade("Unidade Âncora", estudantes));
        atividadesParaSalvar.add(createAtividade("Unidade Farol", estudantes));

        Projeto outros = new Projeto();
        outros.setCategoria("Locais"); outros.setNome("OUTROS PROJETOS");
        projetoRepository.save(outros);
        atividadesParaSalvar.add(createAtividade("Acampamento", outros));
        atividadesParaSalvar.add(createAtividade("Clubão", outros));
        atividadesParaSalvar.add(createAtividade("Fala Sério", outros));
        atividadesParaSalvar.add(createAtividade("Evangelismo em unidades socioeducativas", outros));

        // Agora, salve TODAS as atividades de uma vez
        atividadeRepository.saveAll(atividadesParaSalvar);

        System.out.println(">>> [Ragui Guide] Banco populado com sucesso!");
    }

    /**
     * Método auxiliar para criar uma Atividade, assumindo que a entidade Atividade
     * tem um construtor vazio e seters para 'nome' e 'projeto'.
     * (Assumindo que sua entidade Atividade tem um campo 'projeto' do tipo 'Projeto')
     */
    private Atividade createAtividade(String nome, Projeto projeto) {
        Atividade atividade = new Atividade();
        atividade.setNome(nome);

        // Assumindo que sua entidade Atividade tem um método setProjeto(Projeto projeto)
        // que mapeia para a coluna 'Projeto_idProjeto'
        atividade.setProjeto(projeto);

        return atividade;
    }
}