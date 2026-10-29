package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.interfaces.IContratService;
import tn.esprit.autoloc.web.dto.ContratDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDto.Response create(ContratDto.Request request) {
        log.info("Création d'un nouveau contrat");
        return contratMapper.toResponse(contratRepository.save(contratMapper.toEntity(request)));
    }

    @Override
    public ContratDto.Response findById(Long id) {
        log.info("Recherche du contrat avec l'ID: {}", id);
        return contratMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<ContratDto.Response> findAll() {
        log.info("Récupération de tous les contrats");
        return contratRepository.findAll().stream().map(contratMapper::toResponse).toList();
    }

    @Override
    public ContratDto.Response update(Long id, ContratDto.Request request) {
        log.info("Mise à jour du contrat avec l'ID: {}", id);
        Contrat contrat = getEntityById(id);
        contratMapper.updateEntity(request, contrat);
        return contratMapper.toResponse(contratRepository.save(contrat));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du contrat avec l'ID: {}", id);
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    private Contrat getEntityById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }
}
