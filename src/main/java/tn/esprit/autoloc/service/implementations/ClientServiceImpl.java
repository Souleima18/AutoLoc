package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.interfaces.IClientService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client createClient(Client client) {
        log.info("Creating client: {} {}", client.getNom(), client.getPrenom());
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> getClientById(Long id) {
        log.info("Fetching client with id: {}", id);
        return clientRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        log.info("Fetching client with email: {}", email);
        return clientRepository.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + email));
    }

    @Override
    public Client updateClient(Long id, Client client) {
        log.info("Updating client with id: {}", id);
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        existingClient.setNom(client.getNom());
        existingClient.setPrenom(client.getPrenom());
        existingClient.setEmail(client.getEmail());
        existingClient.setTelephone(client.getTelephone());
        existingClient.setNumPermis(client.getNumPermis());

        return clientRepository.save(existingClient);
    }

    @Override
    public void deleteClient(Long id) {
        log.info("Deleting client with id: {}", id);
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean clientExists(Long id) {
        return clientRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countClients() {
        return clientRepository.count();
    }
}