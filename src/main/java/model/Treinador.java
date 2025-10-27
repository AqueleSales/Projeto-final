package model;

/**
 * Classe de Modelo (POJO) que representa a entidade Treinador.
 * (Baseado no PDF, esta é uma entidade separada e não uma especialização de Pessoa).
 */
public class Treinador {

    private int idTreinador;
    private String nome;
    private String cpf;
    private String numeroCertificacaoProfissional;

    // Construtor vazio
    public Treinador() {
    }

    // Construtor para facilitar os testes
    public Treinador(String nome, String cpf, String numeroCertificacaoProfissional) {
        this.nome = nome;
        this.cpf = cpf;
        this.numeroCertificacaoProfissional = numeroCertificacaoProfissional;
    }

    // Getters e Setters
    public int getIdTreinador() {
        return idTreinador;
    }

    public void setIdTreinador(int idTreinador) {
        this.idTreinador = idTreinador;
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

    public String getNumeroCertificacaoProfissional() {
        return numeroCertificacaoProfissional;
    }

    public void setNumeroCertificacaoProfissional(String numeroCertificacaoProfissional) {
        this.numeroCertificacaoProfissional = numeroCertificacaoProfissional;
    }

    @Override
    public String toString() {
        return "Treinador{" +
                "idTreinador=" + idTreinador +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", numeroCertificacaoProfissional='" + numeroCertificacaoProfissional + '\'' +
                '}';
    }
}
