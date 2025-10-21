package model;

/**
 * Classe de Modelo que representa a entidade Animal_de_Servico.
 * Herda de Pet e adiciona atributos específicos.
 */
public class AnimalDeServico extends Pet {

    private String numeroRegistroOficial;
    private String status; // Ex: 'Ativo', 'Em Treinamento', 'Aposentado'

    public AnimalDeServico() {
        super();
    }

    // Getters e Setters
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
                "pet=" + super.toString() +
                ", numeroRegistroOficial='" + numeroRegistroOficial + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
