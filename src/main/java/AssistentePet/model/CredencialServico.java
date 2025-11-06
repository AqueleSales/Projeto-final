package AssistentePet.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CredencialServico {

    private int idCredencial;
    private LocalDate dataEmissao;
    private LocalDate dataValidade;

    // chaves Estrangeiras (IDs)
    private int idAnimalServico; // (FK para Animal_de_Servico)
    private int idTreinador;     // (FK para Treinador)

    private List<Habilidade> habilidades;

    // construtor
    public CredencialServico() {
        this.habilidades = new ArrayList<>();
    }

    // getters e setters
    public int getIdCredencial() {
        return idCredencial;
    }

    public void setIdCredencial(int idCredencial) {
        this.idCredencial = idCredencial;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public int getIdAnimalServico() {
        return idAnimalServico;
    }

    public void setIdAnimalServico(int idAnimalServico) {
        this.idAnimalServico = idAnimalServico;
    }

    public int getIdTreinador() {
        return idTreinador;
    }

    public void setIdTreinador(int idTreinador) {
        this.idTreinador = idTreinador;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<Habilidade> habilidades) {
        this.habilidades = habilidades;
    }

    public void addHabilidade(Habilidade habilidade) {
        this.habilidades.add(habilidade);
    }
}
