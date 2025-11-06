package AssistentePet.model;

import java.util.ArrayList;
import java.util.List;

public class Pessoa {

    private int idPessoa;
    private String nome;
    private String cpf;
    private String email;
    private List<String> telefones;
    private String senha;

    // construtor padrão
    public Pessoa() {
        this.telefones = new ArrayList<>();
    }

    // construtor com parâmetros
    public Pessoa(int idPessoa, String nome, String cpf, String email) {
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefones = new ArrayList<>();
    }

    // getters e setters
    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void addTelefone(String telefone) {
        this.telefones.add(telefone);
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "idPessoa=" + idPessoa +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefones=" + telefones +
                '}';
    }

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}