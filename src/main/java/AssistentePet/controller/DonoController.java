package AssistentePet.controller;

import AssistentePet.dao.IPessoaDAO;
import AssistentePet.model.Dono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTANTE
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donos")
public class DonoController {

    private final IPessoaDAO pessoaDAO;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DonoController(IPessoaDAO pessoaDAO, PasswordEncoder passwordEncoder) {
        this.pessoaDAO = pessoaDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Dono> salvarDono(@RequestBody Dono dono) {
        try {
            String senhaCriptografada = passwordEncoder.encode(dono.getSenha());
            dono.setSenha(senhaCriptografada);

            Dono donoSalvo = (Dono) pessoaDAO.salvar(dono);

            return new ResponseEntity<>(donoSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar Dono: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}