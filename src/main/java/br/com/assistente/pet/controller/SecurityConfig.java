package br.com.assistente.pet.controller; // (ou o pacote que você usa)

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
        // Vamos usar o BCrypt, o padrão-ouro para senhas
        return new BCryptPasswordEncoder();
    }

    //
    // --- ESTE É O NOVO MÉTODO QUE LIBERA OS ENDPOINTS ---
    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF (necessário para APIs stateless como a nossa)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configura as regras de autorização
                .authorizeHttpRequests(authorize -> authorize
                        // 3. Permite (libera) C-O-M-P-L-E-T-A-M-E-N-T-E as URLs de registro e login
                        .requestMatchers("/api/donos").permitAll()
                        .requestMatchers("/api/pessoas/login").permitAll()

                        // 4. (Opcional) Diz que qualquer outra URL deve ser autenticada
                        .anyRequest().authenticated()
                )

                // 5. Desabilita o login "Basic" (o pop-up feio do navegador)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}