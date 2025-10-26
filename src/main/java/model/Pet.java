package model;

import java.time.LocalDate;

/**
 * Classe de Modelo (POJO) que representa a entidade Pet.
 * É uma superclasse para AnimalDeServico.
 * Esta versão usa java.time.LocalDate, que é o tipo de data correto
 * para ser usado com o PetDAO.
 */
public class Pet {

    private int idPet;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNascimento; // Usando LocalDate para datas
    private int idDono; // Este campo será usado APENAS para transporte, não para o banco

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

    /**
     * Este campo é usado apenas para transferir o ID do dono
     * do Main para o PetDAO, para que o PetDAO saiba
     * qual ID usar na tabela 'Possui'.
     * @return o ID do dono.
     */
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
                ", dataNascimento=" + dataNascimento +
                ", idDonoTransporte=" + idDono + // Mostra o ID do dono associado
                '}';
    }
}

