package model;

/**
 * Classe de Modelo que representa a entidade Veterinario.
 * Herda de Pessoa e adiciona o atributo específico CRMV.
 */
public class Veterinario extends Pessoa {

    private String crmv;

    // Construtor padrão
    public Veterinario() {
        super();
    }

    // Getters e Setters
    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    @Override
    public String toString() {
        return "Veterinario{" +
                "pessoa=" + super.toString() +
                ", crmv='" + crmv + '\'' +
                '}';
    }
}
