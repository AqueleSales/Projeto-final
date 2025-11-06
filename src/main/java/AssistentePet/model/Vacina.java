package AssistentePet.model;

public class Vacina {

    private int idVacina;
    private String nomeVacina;
    private String tipo;

    // construtores
    public Vacina() {
    }

    public Vacina(String nomeVacina, String tipo) {
        this.nomeVacina = nomeVacina;
        this.tipo = tipo;
    }

    // getters e setters
    public int getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(int idVacina) {
        this.idVacina = idVacina;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Vacina{" +
                "idVacina=" + idVacina +
                ", nomeVacina='" + nomeVacina + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

