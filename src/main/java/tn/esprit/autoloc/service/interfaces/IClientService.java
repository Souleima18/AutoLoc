package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Client;
import java.util.List;
import java.util.Optional;

public interface IClientService {

    // CREATE
    Client createClient(Client client);

    // READ
    Optional<Client> getClientById(Long id);
    List<Client> getAllClients();
    Client getClientByEmail(String email);

    // UPDATE
    Client updateClient(Long id, Client client);

    // DELETE
    void deleteClient(Long id);
    boolean clientExists(Long id);
    long countClients();
}