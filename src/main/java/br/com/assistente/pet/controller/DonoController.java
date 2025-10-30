// Arquivo: DonoController.java
package br.com.assistente.pet.controller;

import dao.IPessoaDAO;
import model.Dono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTANTE
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donos")
public class DonoController {

    private final IPessoaDAO pessoaDAO;

    // --- NOVO ---
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DonoController(IPessoaDAO pessoaDAO, PasswordEncoder passwordEncoder) { // Atualizado
        this.pessoaDAO = pessoaDAO;
        this.passwordEncoder = passwordEncoder; // Injetado
    }

    @PostMapping
    public ResponseEntity<Dono> salvarDono(@RequestBody Dono dono) {
        try {
            // --- A MÁGICA DA CRIPTOGRAFIA ---
            // NUNCA salve a senha pura. Salve o "hash" dela.
            String senhaCriptografada = passwordEncoder.encode(dono.getSenha());
            dono.setSenha(senhaCriptografada);

            // Agora o DAO.salvar() vai salvar a senha criptografada
            Dono donoSalvo = (Dono) pessoaDAO.salvar(dono);

            return new ResponseEntity<>(donoSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar Dono: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}