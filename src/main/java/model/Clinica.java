package model;

/**
 * Classe de Modelo (POJO) que representa a entidade Clinica.
 */
public class Clinica {

    private int idClinica;
    private String nome;
    private String email;
    // Endereço
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;

    // Construtor padrão (usado pelo DAO)
    public Clinica() {
    }

    // --- NOVO CONSTRUTOR ---
    // Este é o construtor que o main.java precisa para criar uma nova clínica
    public Clinica(String nome, String email, String rua, String numero, String bairro, String cidade, String cep) {
        this.nome = nome;
        this.email = email;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
    }

    // --- Getters e Setters ---

    public int getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(int idClinica) {
        this.idClinica = idClinica;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return "Clinica{" +
                "idClinica=" + idClinica +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cidade='" + cidade + '\'' +
                '}';
    }
}

