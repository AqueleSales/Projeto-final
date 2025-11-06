package AssistentePet.model;

public class Treinador extends Pessoa {

    private String numeroCertificacaoProfissional;

    // construtor vazio
    public Treinador() {
        super(); // Chama o construtor da classe Pessoa
    }

    // getters e setters
    public String getNumeroCertificacaoProfissional() {
        return numeroCertificacaoProfissional;
    }

    public void setNumeroCertificacaoProfissional(String numeroCertificacaoProfissional) {
        this.numeroCertificacaoProfissional = numeroCertificacaoProfissional;
    }

    @Override
    public String toString() {
        return "Treinador{" +
                "pessoaInfo=" + super.toString() +
                ", numeroCertificacaoProfissional='" + numeroCertificacaoProfissional + '\'' +
                '}';
    }
}