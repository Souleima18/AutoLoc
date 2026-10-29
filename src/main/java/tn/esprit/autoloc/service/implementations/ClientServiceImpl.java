package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.interfaces.IClientService;
import tn.esprit.autoloc.web.dto.ClientDto;
import tn.esprit.autoloc.web.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDto.Response create(ClientDto.Request request) {
        log.info("Création d'un nouveau client");
        return clientMapper.toResponse(clientRepository.save(clientMapper.toEntity(request)));
    }

    @Override
    public ClientDto.Response findById(Long id) {
        log.info("Recherche du client avec l'ID: {}", id);
        return clientMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<ClientDto.Response> findAll() {
        log.info("Récupération de tous les clients");
        return clientRepository.findAll().stream().map(clientMapper::toResponse).toList();
    }

    @Override
    public ClientDto.Response update(Long id, ClientDto.Request request) {
        log.info("Mise à jour du client avec l'ID: {}", id);
        Client client = getEntityById(id);
        clientMapper.updateEntity(request, client);
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du client avec l'ID: {}", id);
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private Client getEntityById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }
}
