package com.fiap.mercadoexpresssec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_SENHA = "senha";

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    public SecurityConfig(LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rotas publicas: landing page, login, cadastro (Sign Up), recursos estaticos
                        .requestMatchers("/", "/index", "/login", "/cadastro", "/acesso-negado", "/error").permitAll()
                        .requestMatchers("/css/**", "/img/**", "/favicon.ico").permitAll()
                        // Somente ADMIN: painel e operacoes de escrita do CRUD (Create, Update, Delete)
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/mercado/novo", "/mercado/*/editar", "/mercado/*/excluir").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/mercado/**").hasRole("ADMIN")
                        // ADMIN e CLIENTE: consulta de produtos (Read)
                        .requestMatchers(HttpMethod.GET, "/mercado", "/mercado/*").hasAnyRole("ADMIN", "CLIENTE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter(PARAM_EMAIL)
                        .passwordParameter(PARAM_SENHA)
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"));

        return http.build();
    }

}
