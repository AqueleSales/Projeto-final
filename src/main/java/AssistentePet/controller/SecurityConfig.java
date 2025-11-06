package AssistentePet.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/*.html", "/css/**").permitAll()
                        .requestMatchers("/api/donos").permitAll()
                        .requestMatchers("/api/veterinarios").permitAll()
                        .requestMatchers("/api/treinadores").permitAll()
                        .requestMatchers("/api/pessoas/login").permitAll()
                        .requestMatchers("/api/pessoas/cpf/**").permitAll()
                        .requestMatchers("/api/pets/**").permitAll()
                        .requestMatchers("/api/animais-de-servico").permitAll()
                        .requestMatchers("/api/pets/promover").permitAll()
                        .requestMatchers("/api/pets/demover/**").permitAll()
                        .requestMatchers("/api/certificados/**").permitAll()
                        .requestMatchers("/api/clinicas/**").permitAll()
                        .requestMatchers("/api/credenciais/**").permitAll()
                        .requestMatchers("/api/habilidades/**").permitAll()
                        .requestMatchers("/api/vacinas/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }
}