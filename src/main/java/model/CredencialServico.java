package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Modelo (POJO) que representa a entidade Credencial_Servico.
 * Esta classe também armazena a lista de Habilidades associadas
 * para facilitar a relação M:N.
 */
public class CredencialServico {

    private int idCredencial;
    private LocalDate dataEmissao;
    private LocalDate dataValidade;

    // Chaves Estrangeiras (IDs)
    private int idAnimalServico; // (FK para Animal_de_Servico)
    private int idTreinador;     // (FK para Treinador)

    // Lista para a relação M:N com Habilidade
    private List<Habilidade> habilidades;

    // Construtor
    public CredencialServico() {
        this.habilidades = new ArrayList<>();
    }

    // Getters e Setters
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

    // Métodos para a lista de Habilidades
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
