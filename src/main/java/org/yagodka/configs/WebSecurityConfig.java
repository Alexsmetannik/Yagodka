package org.yagodka.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.yagodka.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //Доступ только для не зарегистрированных пользователей
                        .requestMatchers("/registration").not().fullyAuthenticated()
                        //Доступ только для пользователей с ролью Администратор
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/news").hasRole("USER")
                        //Доступ разрешен всем пользователей
                        .requestMatchers("/", "/resources/**").permitAll()
                        //Все остальные страницы требуют аутентификации
                        .anyRequest().authenticated()
                )
                //Настройка для входа в систему
                .formLogin(form -> form
                        .loginPage("/login")
                        //Перенарпавление на главную страницу после успешного входа
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/")
                );
        return httpSecurity.build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}
