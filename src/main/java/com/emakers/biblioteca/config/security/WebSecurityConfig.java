package com.emakers.biblioteca.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/persons/savePerson").hasRole("ADMIN")
                                .requestMatchers("/api/persons/getAllPersons").permitAll()
                                .requestMatchers("/api/persons/getOnePerson/**").permitAll()
                                .requestMatchers("/api/persons/updatePerson/**").hasRole("ADMIN")
                                .requestMatchers("/api/persons/deletePerson/**").hasRole("ADMIN")
                                .requestMatchers("/api/books/saveBook").hasRole("ADMIN")
                                .requestMatchers("/api/books/getAllBooks").permitAll()
                                .requestMatchers("/api/books/getOneBook/**").permitAll()
                                .requestMatchers("/api/books/updateBook/**").hasRole("ADMIN")
                                .requestMatchers("/api/books/deleteBook/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                );
                //.formLogin(Customizer.withDefaults())
                //.logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}