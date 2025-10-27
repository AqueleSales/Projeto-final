package model;

import java.time.LocalDate;

/**
 * Classe de Modelo (POJO) que representa a entidade AnimalDeServico.
 * Esta é uma especialização da classe Pet.
 */
public class AnimalDeServico extends Pet {

    private String numeroRegistroOficial;
    private String status; // Ex: 'Ativo', 'Em Treinamento', 'Aposentado'

    // Construtor
    public AnimalDeServico() {
        super(); // Chama o construtor da superclasse Pet
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
                "petInfo=" + super.toString() +
                ", numeroRegistroOficial='" + numeroRegistroOficial + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

