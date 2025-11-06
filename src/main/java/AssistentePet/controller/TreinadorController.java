package AssistentePet.controller;

import AssistentePet.dao.IPessoaDAO;
import AssistentePet.model.Treinador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/treinadores")
public class TreinadorController {

    private final IPessoaDAO pessoaDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TreinadorController(IPessoaDAO pessoaDAO, PasswordEncoder passwordEncoder) {
        this.pessoaDAO = pessoaDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Treinador> salvarTreinador(@RequestBody Treinador treinador) {
        try {
            String senhaCriptografada = passwordEncoder.encode(treinador.getSenha());
            treinador.setSenha(senhaCriptografada);
            Treinador treinadorSalvo = (Treinador) pessoaDAO.salvar(treinador);

            return new ResponseEntity<>(treinadorSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar Treinador: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}