package AssistentePet.controller;

import AssistentePet.dao.IPessoaDAO;
import AssistentePet.model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final IPessoaDAO pessoaDAO;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PessoaController(IPessoaDAO pessoaDAO, PasswordEncoder passwordEncoder) {
        this.pessoaDAO = pessoaDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaDAO.listarTodos();
        return new ResponseEntity<>(pessoas, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPessoaPorId(@PathVariable("id") int id) {
        Pessoa pessoa = pessoaDAO.buscarPorId(id);
        if (pessoa != null) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable("id") int id, @RequestBody Pessoa pessoa) {
        pessoa.setIdPessoa(id);
        boolean atualizou = pessoaDAO.atualizar(pessoa);
        if (atualizou) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable("id") int id) {
        boolean deletou = pessoaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Pessoa> buscarPessoaPorCpf(@PathVariable("cpf") String cpf) {
        Pessoa pessoa = pessoaDAO.buscarPorCpf(cpf);
        if (pessoa != null) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Pessoa> login(@RequestBody Pessoa loginData) {
        Pessoa pessoaNoBanco = pessoaDAO.buscarPorEmail(loginData.getEmail());

        if (pessoaNoBanco != null) {

            String senhaDoApp = loginData.getSenha();
            String senhaDoBanco = pessoaNoBanco.getSenha();

            if (passwordEncoder.matches(senhaDoApp, senhaDoBanco)) {
                return new ResponseEntity<>(pessoaNoBanco, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Não Autorizado
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Não Encontrado
        }
    }
}