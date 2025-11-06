package AssistentePet.model;

public class Veterinario extends AssistentePet.model.Pessoa {

    private String crmv;

    // construtores
    public Veterinario() {
        super(); // Chama o construtor da classe Pessoa
    }

    // getters e setters específicos do Veterinario
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

