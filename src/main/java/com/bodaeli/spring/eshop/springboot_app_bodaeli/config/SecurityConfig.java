package com.bodaeli.spring.eshop.springboot_app_bodaeli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // El login del admin es público
                .requestMatchers("/api/admin/login").permitAll()
                // Endpoints del panel real → protegidos
                .requestMatchers("/api/admin/**").authenticated()
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/api/regalos/**", "/api/compras/**").permitAll()
                .anyRequest().permitAll()
            )
            // Desactivar los mecanismos de login por defecto de Spring Security
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}