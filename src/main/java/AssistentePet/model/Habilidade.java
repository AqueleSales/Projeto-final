package AssistentePet.model;

public class Habilidade {

    private int idHabilidade;
    private String descricaoHabilidade;

    // construtores
    public Habilidade() {
    }

    public Habilidade(String descricaoHabilidade) {
        this.descricaoHabilidade = descricaoHabilidade;
    }

    // getters e setters
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
