package model;

import java.time.LocalDate;

/**
 * Classe de Modelo (POJO) que representa a entidade CertificadoVacina.
 * Esta é a entidade associativa que registra uma vacinação.
 */
public class CertificadoVacina {

    private int idCertificadoVac;
    private LocalDate dataAplicacao;
    private String lote;
    private LocalDate proximaDose;

    // Chaves Estrangeiras
    private int idPet;
    private int idVacina;
    private int idVeterinario;
    private int idClinica;

    // Construtor
    public CertificadoVacina() {
    }

    // Getters e Setters

    public int getIdCertificadoVac() {
        return idCertificadoVac;
    }

    public void setIdCertificadoVac(int idCertificadoVac) {
        this.idCertificadoVac = idCertificadoVac;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(LocalDate proximaDose) {
        this.proximaDose = proximaDose;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public int getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(int idVacina) {
        this.idVacina = idVacina;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public int getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(int idClinica) {
        this.idClinica = idClinica;
    }

    @Override
    public String toString() {
        return "CertificadoVacina{" +
                "idCertificadoVac=" + idCertificadoVac +
                ", dataAplicacao=" + dataAplicacao +
                ", lote='" + lote + '\'' +
                ", proximaDose=" + proximaDose +
                ", idPet=" + idPet +
                ", idVacina=" + idVacina +
                ", idVeterinario=" + idVeterinario +
                ", idClinica=" + idClinica +
                '}';
    }
}
