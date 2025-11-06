package AssistentePet.dao;

import AssistentePet.model.CredencialServico;

public interface ICredencialServicoDAO {

    CredencialServico salvar(CredencialServico credencial);

    CredencialServico buscarPorAnimalId(int idAnimal);

    CredencialServico buscarPorId(int id);

    boolean deletar(int id);
}