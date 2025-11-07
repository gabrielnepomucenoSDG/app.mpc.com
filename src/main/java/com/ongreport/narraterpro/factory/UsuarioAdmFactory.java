package com.ongreport.narraterpro.factory;

import com.ongreport.narraterpro.entity.Cidade;
import com.ongreport.narraterpro.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component // Marca como um componente Spring para injeção de dependência
public class UsuarioAdmFactory implements UsuarioFactory {

    private final PasswordEncoder passwordEncoder;

    // Injeta o PasswordEncoder no construtor
    @Autowired
    public UsuarioAdmFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario criarUsuario(String nome, String email, String senha, Cidade cidade) {
        Usuario user = new Usuario();
        user.setNome(nome);
        user.setEmail(email);
        user.setSenha(passwordEncoder.encode(senha)); // Criptografa a senha!
        user.setAdm(1); // Padrão: usuário comum
        user.setCidade(cidade); // Associa a cidade
        // Defina outros valores padrão ou campos obrigatórios aqui
        return user;
    }
}