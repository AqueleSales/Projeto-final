package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Modelo (POJO) que representa a entidade Pessoa.
 * ATUALIZADO: Inclui o campo 'senha'
 */
public class Pessoa {

    private int idPessoa;
    private String nome;
    private String cpf;
    private String email;
    private List<String> telefones; // Para o atributo multivalorado

    // --- NOVO CAMPO ADICIONADO ---
    private String senha;

    // Construtor padrão
    public Pessoa() {
        this.telefones = new ArrayList<>();
    }

    // Construtor com parâmetros
    public Pessoa(int idPessoa, String nome, String cpf, String email) {
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefones = new ArrayList<>();
    }

    // Getters e Setters
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

    // --- GETTER E SETTER PARA O NOVO CAMPO 'SENHA' ---
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    // ----------------------------------------------------

    // Método para adicionar um telefone à lista
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
}