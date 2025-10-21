package model;

import java.time.LocalDate;

/**
 * Classe de Modelo (POJO) que representa a entidade Pet.
 */
public class Pet {

    private int idPet;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNasc;
    private int idDono; // Chave estrangeira para Dono

    // Construtores, Getters e Setters
    public Pet() {
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public int getIdDono() {
        return idDono;
    }

    public void setIdDono(int idDono) {
        this.idDono = idDono;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "idPet=" + idPet +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", raca='" + raca + '\'' +
                ", dataNasc=" + dataNasc +
                ", idDono=" + idDono +
                '}';
    }
}
