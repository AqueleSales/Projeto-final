package AssistentePet.model;

import java.time.LocalDate;

public class Pet {

    private int idPet;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNascimento;
    private int idDonoTransporte;

    // construtores
    public Pet() {
    }

    // getters e setters
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

    // getters e setters
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

