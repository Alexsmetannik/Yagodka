package org.yagodka.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Публичные endpoints
                        .anyRequest().authenticated() // Все остальные endpoints требуют авторизации
                )
                .formLogin(form -> form
                        .loginPage("/public/login") // Страница авторизации
                        .defaultSuccessUrl("/api/dashboard") // Перенаправление после успешной авторизации
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout") // URL для выхода
                        .logoutSuccessUrl("/public/logout-success") // Перенаправление после выхода
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Создаем in-memory пользователя
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
