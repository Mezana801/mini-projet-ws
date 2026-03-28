package com.example.miniprojetgestionscolaire.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Routes publiques
                        // H2 Console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        // Cours - GET public, POST/PUT/DELETE protégés
                        .requestMatchers(HttpMethod.GET, "/api/cours", "/api/cours/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cours").hasAnyRole("ADMIN", "PROFESSEUR")
                        .requestMatchers(HttpMethod.PUT, "/api/cours/**").hasAnyRole("ADMIN", "PROFESSEUR")
                        .requestMatchers(HttpMethod.DELETE, "/api/cours/**").hasRole("ADMIN")
                        // Etudiants - ADMIN uniquement
                        .requestMatchers("/api/etudiants/**").hasRole("ADMIN")
                        // Professeurs
                        .requestMatchers(HttpMethod.GET, "/api/professeurs", "/api/professeurs/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/professeurs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/professeurs/**").hasAnyRole("ADMIN", "PROFESSEUR")
                        .requestMatchers(HttpMethod.DELETE, "/api/professeurs/**").hasRole("ADMIN")
                        // Notes
                        .requestMatchers("/api/notes/**").hasAnyRole("ADMIN","PROFESSEUR")
                        // Inscriptions
                        .requestMatchers("/api/inscriptions/**").authenticated()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // pour H2 console
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
