package model;

/**
 * Classe de Modelo (POJO) que representa a entidade Habilidade
 * (Habilidades que um animal de serviço pode ter).
 */
public class Habilidade {

    private int idHabilidade;
    private String descricaoHabilidade;

    // Construtores
    public Habilidade() {
    }

    public Habilidade(String descricaoHabilidade) {
        this.descricaoHabilidade = descricaoHabilidade;
    }

    // Getters e Setters
    public int getIdHabilidade() {
        return idHabilidade;
    }

    public void setIdHabilidade(int idHabilidade) {
        this.idHabilidade = idHabilidade;
    }

    public String getDescricaoHabilidade() {
        return descricaoHabilidade;
    }

    public void setDescricaoHabilidade(String descricaoHabilidade) {
        this.descricaoHabilidade = descricaoHabilidade;
    }

    @Override
    public String toString() {
        return "Habilidade{" +
                "idHabilidade=" + idHabilidade +
                ", descricaoHabilidade='" + descricaoHabilidade + '\'' +
                '}';
    }
}
