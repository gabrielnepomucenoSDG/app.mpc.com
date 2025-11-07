package com.ongreport.narraterpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // REGRA 1 (MAIS ESPECÍFICA): Adicionamos esta linha.
                        // Ela cria uma EXCEÇÃO para a regra de admin, permitindo que qualquer
                        // usuário LOGADO acesse sua própria página de perfil.
                        ///////////////////////////////////adicionei abaixo o endpoint /usuarios/novo
                        .requestMatchers("/", "/login", "/usuarios/novo", "/css/**", "/js/**", "/error").permitAll()
                        //////////////////////////////////////// realoquei para as linhas antes do bloqueador de endpoints /usuarios/
                        .requestMatchers("/usuarios/perfil").authenticated()

                        // REGRA 2 (MAIS GERAL): Esta sua regra continua valendo para o resto.
                        // A lista de usuários, deletar, etc., continuam protegidos para admins.
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Suas outras regras existentes
                        .requestMatchers("/projetos/**").hasRole("ADMIN")
                        .requestMatchers("/agendamentos/**").authenticated()
                        .anyRequest().authenticated()
                )
                //... (resto do arquivo sem alterações)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        return http.build();
    }
}