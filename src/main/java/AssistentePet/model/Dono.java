package AssistentePet.model;

public class Dono extends Pessoa {

    // construtor padrão
    public Dono() {
        super(); // Chama o construtor da superclasse Pessoa
    }

    // construtor que recebe os dados de uma Pessoa
    public Dono(Pessoa pessoa) {
        super(pessoa.getIdPessoa(), pessoa.getNome(), pessoa.getCpf(), pessoa.getEmail());
        this.setTelefones(pessoa.getTelefones());
    }
}
