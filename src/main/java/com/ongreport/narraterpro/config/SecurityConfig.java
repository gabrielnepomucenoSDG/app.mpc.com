package com.ongreport.narraterpro.config;

// Importamos o HttpMethod para sermos mais específicos
import org.springframework.http.HttpMethod;
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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // REGRAS DE AUTORIZAÇÃO (A ORDEM IMPORTA!)

                        // 1. Libera URLs estáticas, login, e a PÁGINA de novo usuário
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/error").permitAll()

                        // 2. Libera o GET para VER o formulário de novo usuário
                        .requestMatchers(HttpMethod.GET, "/usuarios/novo").permitAll()

                        // 3. Libera o POST para SALVAR o novo usuário
                        //    (Estou assumindo que seu form action="POST /usuarios")
                        //    Se seu form posta para "/usuarios/novo", mude a URL abaixo.
                        .requestMatchers(HttpMethod.POST, "/usuarios/salvar").permitAll()

                        // 4. Regra específica para a própria página de perfil (usuário logado)
                        .requestMatchers("/usuarios/perfil").authenticated()

                        // 5. Regra GERAL para o resto de "/usuarios" (só admin)
                        //    (Ex: GET /usuarios (listar), DELETE /usuarios/{id}, etc.)
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // 6. Suas outras regras existentes
                        .requestMatchers("/projetos/**").hasRole("ADMIN")
                        .requestMatchers("/agendamentos/**").authenticated()

                        // 7. REGRA FINAL: Qualquer outra requisição, precisa estar logada.
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