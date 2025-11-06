package com.ongreport.narraterpro;

import com.ongreport.narraterpro.entity.Cidade;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.CidadeRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import com.ongreport.narraterpro.factory.UsuarioFactory;
import com.ongreport.narraterpro.service.AgendamentoService; // O Publisher
import com.ongreport.narraterpro.observer.impl.PontuadorUsuarioObserver; // O Observer 2

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NarraterproApplication {

	public static void main(String[] args) {
		SpringApplication.run(NarraterproApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
										  CidadeRepository cidadeRepository,
										  PasswordEncoder passwordEncoder,
										  UsuarioFactory usuarioFactory,
										  AgendamentoService agendamentoService, // Injete o Publisher
										   // Injete os Observers
										  PontuadorUsuarioObserver pontuadorUsuarioObserver) {
		return args -> {
			if (usuarioRepository.findByEmail("admin@ong.com").isEmpty()) {
				Cidade cidadeAdmin = new Cidade();
				cidadeAdmin.setNome("São Luís");
				cidadeAdmin.setEstado("MA");
				cidadeRepository.save(cidadeAdmin);

				// --- PASSO 2: USAR A FÁBRICA PARA CRIAR O USUÁRIO ---
				// A fábrica já vai criptografar a senha e configurar o 'adm'
				Usuario admin = usuarioFactory.criarUsuario("Admin", "admin@ong.com", "123456", cidadeAdmin);

				usuarioRepository.save(admin);
				System.out.println("Usuário admin de teste criado via Factory Method!");
			}
			agendamentoService.addObserver(pontuadorUsuarioObserver);
			System.out.println("Observers de agendamento registrados com sucesso!");
		};
	}
}