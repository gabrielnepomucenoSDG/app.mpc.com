package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.entity.Cidade;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.CidadeRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CidadeRepository cidadeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, CidadeRepository cidadeRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cidadeRepository = cidadeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca todos os usuários cadastrados.
     * @return Uma lista de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca um usuário pelo seu ID.
     * @param id O ID do usuário.
     * @return Um Optional contendo o usuário, se encontrado.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Salva um novo usuário ou atualiza um existente.
     * A senha será codificada antes de ser salva no banco.
     * @param usuario O objeto Usuario a ser salvo.
     * @return O usuário salvo.
     */
    @Transactional
    public Usuario save(Usuario usuario) {
        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Deleta um usuário pelo seu ID.
     * @param id O ID do usuário a ser deletado.
     */
    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Busca todas as cidades para popular formulários.
     * @return Uma lista de todas as cidades.
     */
    @Transactional(readOnly = true)
    public List<Cidade> findAllCidades() {
        return cidadeRepository.findAll();
    }
}