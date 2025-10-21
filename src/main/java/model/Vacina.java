package model;

/**
 * Classe de Modelo (POJO) que representa a entidade Vacina.
 */
public class Vacina {

    private int idVacina;
    private String nomeVacina;
    private String tipo;

    // Construtores, Getters e Setters
    public Vacina() {
    }

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
}
