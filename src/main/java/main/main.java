package main;

// Imports dos DAOs
import dao.*;
// Imports dos Modelos
import model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal para testar TODAS as funcionalidades da camada DAO.
 * Executa o CRUD completo e as especializações.
 */
public class main {

    public static void main(String[] args) {

        // --- INICIALIZAÇÃO DOS DAOs ---
        IPessoaDAO pessoaDAO = new PessoaDAO();
        IPetDAO petDAO = new PetDAO();
        ITreinadorDAO treinadorDAO = new TreinadorDAO();
        IHabilidadeDAO habilidadeDAO = new HabilidadeDAO();
        ICredencialServicoDAO credencialDAO = new CredencialServicoDAO();

        // --- IDs DE CONTROLE PARA LIMPEZA ---
        int idDono = 0;
        int idAnimalServico = 0;
        int idTreinador = 0;
        int idHabilidade1 = 0;
        int idHabilidade2 = 0;
        int idCredencial = 0;

        System.out.println("--- INICIANDO TESTE COMPLETO DO MÓDULO DE ACESSIBILIDADE ---");

        try {
            // --- 1. CRIAR DONO ---
            System.out.println("\n--- Testando: Salvar Dono ---");
            Dono dono = new Dono();
            dono.setNome("Gabriel Sales (Dono Teste)");
            dono.setCpf("999.999.999-99");
            dono.setEmail("gabriel.teste@email.com");
            Pessoa pessoaDono = pessoaDAO.salvar(dono);
            idDono = pessoaDono.getIdPessoa();
            System.out.println("Dono salvo com sucesso! ID: " + idDono);

            // --- 2. CRIAR TREINADOR ---
            System.out.println("\n--- Testando: Salvar Treinador ---");
            Treinador treinador = new Treinador("Carlos Silva", "111.111.111-11", "CERT-TREIN-9988");
            Treinador treinadorSalvo = treinadorDAO.salvar(treinador);
            idTreinador = treinadorSalvo.getIdTreinador();
            System.out.println("Treinador salvo com sucesso! ID: " + idTreinador);

            // --- 3. CRIAR HABILIDADES ---
            System.out.println("\n--- Testando: Salvar Habilidades ---");
            Habilidade hab1 = new Habilidade("Guiar em via pública");
            Habilidade hab1Salva = habilidadeDAO.salvar(hab1);
            idHabilidade1 = hab1Salva.getIdHabilidade();
            System.out.println("Habilidade 1 salva! ID: " + idHabilidade1);

            Habilidade hab2 = new Habilidade("Alerta de hipoglicemia");
            Habilidade hab2Salva = habilidadeDAO.salvar(hab2);
            idHabilidade2 = hab2Salva.getIdHabilidade();
            System.out.println("Habilidade 2 salva! ID: " + idHabilidade2);

            // --- 4. CRIAR ANIMAL DE SERVIÇO ---
            System.out.println("\n--- Testando: Salvar Animal de Serviço ---");
            AnimalDeServico animalServico = new AnimalDeServico();
            animalServico.setNome("Max");
            animalServico.setEspecie("Cão");
            animalServico.setRaca("Labrador");
            animalServico.setDataNascimento(LocalDate.of(2021, 3, 20));
            animalServico.setIdDonoTransporte(idDono);
            animalServico.setNumeroRegistroOficial("REG-AS-554433");
            animalServico.setStatus("Ativo");
            Pet animalServicoSalvo = petDAO.salvar(animalServico);
            idAnimalServico = animalServicoSalvo.getIdPet();
            System.out.println("Animal de Serviço salvo com sucesso! ID: " + idAnimalServico);

            // --- 5. CRIAR CREDENCIAL (Juntando tudo) ---
            System.out.println("\n--- Testando: Salvar Credencial de Serviço ---");
            CredencialServico credencial = new CredencialServico();
            credencial.setDataEmissao(LocalDate.now());
            credencial.setDataValidade(LocalDate.now().plusYears(2));
            credencial.setIdAnimalServico(idAnimalServico);
            credencial.setIdTreinador(idTreinador);
            // Adicionando as habilidades M:N
            credencial.addHabilidade(hab1Salva);
            credencial.addHabilidade(hab2Salva);

            CredencialServico credencialSalva = credencialDAO.salvar(credencial);
            idCredencial = credencialSalva.getIdCredencial();
            System.out.println("Credencial salva com sucesso! ID: " + idCredencial);

            // --- 6. TESTE DE BUSCA (A PROVA FINAL) ---
            System.out.println("\n--- Testando: Buscar Credencial por ID do Animal ---");
            CredencialServico credencialBuscada = credencialDAO.buscarPorIdAnimal(idAnimalServico);

            if (credencialBuscada == null) {
                throw new RuntimeException("ERRO FATAL: Credencial não foi encontrada!");
            }
            if (credencialBuscada.getHabilidades().size() != 2) {
                throw new RuntimeException("ERRO FATAL: Relação M:N falhou, habilidades não foram carregadas!");
            }

            System.out.println("SUCESSO: Credencial encontrada!");
            System.out.println("   -> ID da Credencial: " + credencialBuscada.getIdCredencial());
            System.out.println("   -> ID do Animal: " + credencialBuscada.getIdAnimalServico());
            System.out.println("   -> ID do Treinador: " + credencialBuscada.getIdTreinador());
            System.out.println("   -> Habilidades Carregadas (" + credencialBuscada.getHabilidades().size() + "):");
            for(Habilidade h : credencialBuscada.getHabilidades()) {
                System.out.println("      - " + h.getDescricaoHabilidade());
            }

        } catch (Exception e) {
            System.err.println("ERRO FATAL DURANTE OS TESTES: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // --- 7. TESTE DE DELETAR (LIMPEZA) ---
            System.out.println("\n--- Testando: Deletar (Limpeza) ---");

            // A ordem de deleção é importante se não houver CASCADE
            // 1. Deletar Credencial (CASCADE deleta Credencial_Habilidade)
            if (idCredencial > 0) {
                if (credencialDAO.deletar(idCredencial)) System.out.println("Deleção da Credencial: SUCESSO");
                else System.err.println("Deleção da Credencial: FALHA");
            }

            // 2. Deletar Animal (CASCADE deleta AnimalDeServico e Possui)
            if (idAnimalServico > 0) {
                if (petDAO.deletar(idAnimalServico)) System.out.println("Deleção do Animal de Serviço: SUCESSO");
                else System.err.println("Deleção do Animal de Serviço: FALHA");
            }

            // 3. Deletar Dono (CASCADE deleta Pessoa)
            if (idDono > 0) {
                if (pessoaDAO.deletar(idDono)) System.out.println("Deleção do Dono: SUCESSO");
                else System.err.println("Deleção do Dono: FALHA");
            }

            // 4. Deletar Treinador
            if (idTreinador > 0) {
                if (treinadorDAO.deletar(idTreinador)) System.out.println("Deleção do Treinador: SUCESSO");
                else System.err.println("Deleção do Treinador: FALHA");
            }

            // 5. Deletar Habilidades
            if (idHabilidade1 > 0) {
                if (habilidadeDAO.deletar(idHabilidade1)) System.out.println("Deleção da Habilidade 1: SUCESSO");
                else System.err.println("Deleção da Habilidade 1: FALHA");
            }
            if (idHabilidade2 > 0) {
                if (habilidadeDAO.deletar(idHabilidade2)) System.out.println("Deleção da Habilidade 2: SUCESSO");
                else System.err.println("Deleção da Habilidade 2: FALHA");
            }

            System.out.println("\n--- TESTES CONCLUÍDOS ---");
        }
    }
}

