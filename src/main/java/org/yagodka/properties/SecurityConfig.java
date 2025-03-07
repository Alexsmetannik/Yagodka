package org.yagodka.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Доступ без авторизации
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница авторизации
                        .defaultSuccessUrl("/dashboard") // Перенаправление после успешной авторизации
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll()
                );
        return http.build();
    }
}
