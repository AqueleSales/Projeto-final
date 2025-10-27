package model;

import java.time.LocalDate;

/**
 * Classe de Modelo (POJO) que representa a entidade Pet.
 * Esta é uma superclasse para AnimalDeServico.
 * Esta versão usa java.time.LocalDate, que é o tipo de data correto
 * para ser usado com o PetDAO.
 */
public class Pet {

    private int idPet;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNascimento;

    /**
     * Este campo será usado APENAS para transporte, não para o banco.
     * O main.java usa o setIdDonoTransporte() para passar o ID do dono,
     * e o PetDAO usa o getIdDonoTransporte() para salvá-lo na tabela 'Possui'.
     */
    private int idDonoTransporte;

    // Construtores
    public Pet() {
    }

    // Getters e Setters (Corretos)
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters para o campo de transporte
    public int getIdDonoTransporte() {
        return idDonoTransporte;
    }

    public void setIdDonoTransporte(int idDonoTransporte) {
        this.idDonoTransporte = idDonoTransporte;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "idPet=" + idPet +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", raca='" + raca + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", idDonoTransporte=" + idDonoTransporte +
                '}';
    }
}

