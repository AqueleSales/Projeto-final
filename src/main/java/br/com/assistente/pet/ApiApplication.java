package br.com.assistente.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Classe principal que inicia a API do Spring Boot.
 * Esta classe é o ponto de entrada que "liga" o servidor web.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"conexãoBD", "dao", "model", "br.com.assistente.pet"})
public class ApiApplication {

    public static void main(String[] args) {
        // Esta linha inicia o servidor Tomcat e toda a sua aplicação
        SpringApplication.run(ApiApplication.class, args);

        System.out.println("\n*** API Meu Assistente Pet iniciada! ***");
        System.out.println("*** Pronta para receber requisições do app celular em http://localhost:8080 ***\n");
    }
}
