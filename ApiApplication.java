package AssistentePet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        System.out.println("\n*** Deu bom a API***");
        System.out.println("*** Pronta para receber requisições em http://localhost:8080 ***\n");
    }
}