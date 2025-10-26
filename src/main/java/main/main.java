package main;

import dao.*; // Importa todos os DAOs de uma vez
import model.*; // Importa todos os Modelos de uma vez

import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal para testar as funcionalidades da camada DAO.
 * Testa o CRUD de Pessoa (Dono e Veterinario), Pet, Clinica e Vacina.
 */
public class main {

    public static void main(String[] args) {

        // --- TESTE DA CLINICA ---
        System.out.println("--- Testando: Salvar Clínica ---");
        IClinicaDAO clinicaDAO = new ClinicaDAO();
        Clinica novaClinica = new Clinica();
        novaClinica.setNome("PetSaúde Center");
        novaClinica.setEmail("contato@petsaudecenter.com");
        novaClinica.setRua("Rua das Palmeiras");
        novaClinica.setNumero("123");
        novaClinica.setBairro("Centro");
        novaClinica.setCidade("Brasília");
        novaClinica.setCep("70000-000");

        Clinica clinicaSalva = clinicaDAO.salvar(novaClinica);
        int idClinica = (clinicaSalva != null) ? clinicaSalva.getIdClinica() : 0;
        if (idClinica > 0) {
            System.out.println("Clínica salva com sucesso! ID: " + idClinica);
        } else {
            System.err.println("Falha ao salvar a clínica.");
            return; // Encerra se não puder salvar a clínica
        }
        System.out.println("\n-----------------------------------\n");


        // --- TESTE DO VETERINARIO ---
        System.out.println("--- Testando: Salvar Veterinario ---");
        IPessoaDAO pessoaDAO = new PessoaDAO();
        Veterinario vet = new Veterinario();
        vet.setNome("Dra. Ana Costa");
        vet.setCpf("111.222.333-44");
        vet.setEmail("ana.costa@clinica.com");
        vet.setCrmv("CRMV-DF 12345");
        vet.addTelefone("6198888-7777");

        Pessoa pessoaVetSalva = pessoaDAO.salvar(vet);
        int idVeterinario = (pessoaVetSalva != null) ? pessoaVetSalva.getIdPessoa() : 0;
        if (idVeterinario > 0) {
            System.out.println("Veterinario salvo com sucesso! ID: " + idVeterinario);
        } else {
            System.err.println("Falha ao salvar o Veterinario.");
            return; // Encerra
        }
        System.out.println("\n-----------------------------------\n");


        // --- TESTE DO DONO E PET ---
        System.out.println("--- Testando: Salvar Dono ---");
        Dono novoDono = new Dono();
        novoDono.setNome("Gabriel Sales");
        novoDono.setCpf("123.456.789-00");
        novoDono.setEmail("gabriel.sales@email.com");
        novoDono.addTelefone("6199999-8888");

        Pessoa pessoaDonoSalva = pessoaDAO.salvar(novoDono);
        int idDono = (pessoaDonoSalva != null) ? pessoaDonoSalva.getIdPessoa() : 0;
        if (idDono > 0) {
            System.out.println("Dono salvo com sucesso! ID: " + idDono);
        } else {
            System.err.println("Falha ao salvar o Dono.");
            return; // Encerra
        }
        System.out.println("\n-----------------------------------\n");

        System.out.println("--- Testando: Salvar Pet ---");
        IPetDAO petDAO = new PetDAO();
        Pet novoPet = new Pet();
        novoPet.setNome("Bob");
        novoPet.setEspecie("Cachorro");
        novoPet.setRaca("Golden Retriever");
        novoPet.setDataNascimento(LocalDate.of(2020, 5, 15));
        novoPet.setIdDono(idDono);

        Pet petSalvo = petDAO.salvar(novoPet);
        int idPet = (petSalvo != null) ? petSalvo.getIdPet() : 0;
        if (idPet > 0) {
            System.out.println("Pet salvo com sucesso! ID: " + idPet);
        } else {
            System.err.println("Falha ao salvar o pet.");
            return; // Encerra
        }
        System.out.println("\n-----------------------------------\n");


        // --- TESTE DA VACINA ---
        System.out.println("--- Testando: Salvar Vacina ---");
        IVacinaDAO vacinaDAO = new VacinaDAO();
        Vacina novaVacina = new Vacina();
        novaVacina.setNomeVacina("Antirrábica");
        novaVacina.setTipo("Obrigatória");

        Vacina vacinaSalva = vacinaDAO.salvar(novaVacina);
        int idVacina = (vacinaSalva != null) ? vacinaSalva.getIdVacina() : 0;
        if (idVacina > 0) {
            System.out.println("Vacina salva com sucesso! ID: " + idVacina);
        } else {
            System.err.println("Falha ao salvar a vacina.");
            return; // Encerra
        }
        System.out.println("\n-----------------------------------\n");

        // --- Testando: Buscar Vacina por ID ---
        System.out.println("--- Testando: Buscar Vacina por ID (" + idVacina + ") ---");
        Vacina vacinaEncontrada = vacinaDAO.buscarPorId(idVacina);
        if (vacinaEncontrada != null) {
            System.out.println("Vacina encontrada: " + vacinaEncontrada.getNomeVacina());
        } else {
            System.err.println("Falha ao buscar vacina.");
        }
        System.out.println("\n-----------------------------------\n");


        // --- Testando: Atualizar Vacina ---
        System.out.println("--- Testando: Atualizar Vacina ---");
        vacinaEncontrada.setTipo("Obrigatória Anual");
        boolean vacinaAtualizou = vacinaDAO.atualizar(vacinaEncontrada);
        if (vacinaAtualizou) {
            System.out.println("Vacina atualizada com sucesso!");
        } else {
            System.err.println("Falha ao atualizar vacina.");
        }
        System.out.println("\n-----------------------------------\n");


        // --- TESTE DE DELETAR (LIMPEZA) ---
        // A ordem de deleção é importante por causa das chaves estrangeiras (FKs)
        System.out.println("--- Testando: Deletar (Limpeza) ---");

        if (idPet > 0) {
            boolean petDeletado = petDAO.deletar(idPet);
            System.out.println("Deleção do Pet: " + (petDeletado ? "SUCESSO" : "FALHA"));
        }
        if (idDono > 0) {
            boolean donoDeletado = pessoaDAO.deletar(idDono);
            System.out.println("Deleção do Dono: " + (donoDeletado ? "SUCESSO" : "FALHA"));
        }
        if (idVeterinario > 0) {
            boolean vetDeletado = pessoaDAO.deletar(idVeterinario);
            System.out.println("Deleção do Veterinario: " + (vetDeletado ? "SUCESSO" : "FALHA"));
        }
        if (idVacina > 0) {
            boolean vacinaDeletada = vacinaDAO.deletar(idVacina);
            System.out.println("Deleção da Vacina: " + (vacinaDeletada ? "SUCESSO" : "FALHA"));
        }
        if (idClinica > 0) {
            boolean clinicaDeletada = clinicaDAO.deletar(idClinica);
            System.out.println("Deleção da Clínica: " + (clinicaDeletada ? "SUCESSO" : "FALHA"));
        }
    }
}

