package model;

/**
 * Classe de Modelo (POJO) que representa a entidade Veterinario.
 * Esta é uma subclasse de Pessoa e adiciona o campo CRMV.
 */
public class Veterinario extends Pessoa {

    private String crmv;

    // Construtores
    public Veterinario() {
        super(); // Chama o construtor da classe Pessoa
    }

    // Getters e Setters específicos do Veterinario
    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    @Override
    public String toString() {
        // Usa o toString() da Pessoa e adiciona o CRMV
        return "Veterinario{" +
                "pessoa=" + super.toString() +
                ", crmv='" + crmv + '\'' +
                '}';
    }
}

