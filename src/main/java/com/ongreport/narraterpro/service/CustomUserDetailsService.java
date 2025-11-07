package com.ongreport.narraterpro.service;

import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        // Define o papel (role) do usuário. Simples por enquanto.
        // O Spring Security exige que os papéis comecem com "ROLE_".
        String role = "ROLE_USER";
        if (usuario.getAdm() != null && usuario.getAdm() == 1) { // Supondo que 'adm' seja 1 para admin
            role = "ROLE_ADMIN";
        }

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}