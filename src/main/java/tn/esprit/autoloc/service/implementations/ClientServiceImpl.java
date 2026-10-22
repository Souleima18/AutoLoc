package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.interfaces.IClientService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        log.info("Création d'un nouveau client");
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        if (client.getNom() == null || client.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email du client est obligatoire");
        }
        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        log.info("Recherche du client avec l'ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public List<Client> findAll() {
        log.info("Récupération de tous les clients");
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        log.info("Mise à jour du client avec l'ID: {}", id);
        Client existant = findById(id);

        if (client.getNom() != null && !client.getNom().isBlank()) {
            existant.setNom(client.getNom());
        }
        if (client.getPrenom() != null && !client.getPrenom().isBlank()) {
            existant.setPrenom(client.getPrenom());
        }
        if (client.getEmail() != null && !client.getEmail().isBlank()) {
            existant.setEmail(client.getEmail());
        }
        if (client.getTelephone() != null && !client.getTelephone().isBlank()) {
            existant.setTelephone(client.getTelephone());
        }

        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du client avec l'ID: {}", id);
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }
}