package AssistentePet.controller;

import AssistentePet.dao.IPessoaDAO;
import AssistentePet.model.Veterinario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    private final IPessoaDAO pessoaDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public VeterinarioController(IPessoaDAO pessoaDAO, PasswordEncoder passwordEncoder) {
        this.pessoaDAO = pessoaDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Veterinario> salvarVeterinario(@RequestBody Veterinario veterinario) {
        try {
            String senhaCriptografada = passwordEncoder.encode(veterinario.getSenha());
            veterinario.setSenha(senhaCriptografada);

            Veterinario vetSalvo = (Veterinario) pessoaDAO.salvar(veterinario);

            return new ResponseEntity<>(vetSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar Veterinario: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}