package model;

/**
 * Classe de Modelo que representa a entidade Dono.
 * Herda todos os atributos da classe Pessoa.
 * Esta classe é uma especialização e serve para tipificar um tipo de Pessoa no sistema.
 */
public class Dono extends Pessoa {

    // Construtor padrão
    public Dono() {
        super(); // Chama o construtor da superclasse Pessoa
    }

    // Construtor que recebe os dados de uma Pessoa
    public Dono(Pessoa pessoa) {
        super(pessoa.getIdPessoa(), pessoa.getNome(), pessoa.getCpf(), pessoa.getEmail());
        this.setTelefones(pessoa.getTelefones());
    }
}
