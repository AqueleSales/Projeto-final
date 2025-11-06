package AssistentePet.model;

public class AnimalDeServico extends Pet {

    private String numeroRegistroOficial;
    private String status;

    // construtor
    public AnimalDeServico() {
        super(); // Chama o construtor da superclasse Pet
    }

    // getters e setters
    public String getNumeroRegistroOficial() {
        return numeroRegistroOficial;
    }

    public void setNumeroRegistroOficial(String numeroRegistroOficial) {
        this.numeroRegistroOficial = numeroRegistroOficial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AnimalDeServico{" +
                "petInfo=" + super.toString() +
                ", numeroRegistroOficial='" + numeroRegistroOficial + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

