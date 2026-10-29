package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDto;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDto.Response create(AgenceDto.Request request) {
        log.info("Création d'une nouvelle agence");
        return agenceMapper.toResponse(agenceRepository.save(agenceMapper.toEntity(request)));
    }

    @Override
    public AgenceDto.Response findById(Long id) {
        log.info("Recherche de l'agence avec l'ID: {}", id);
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        return agenceMapper.toResponse(agence);
    }

    @Override
    public List<AgenceDto.Response> findAll() {
        log.info("Récupération de toutes les agences");
        return agenceRepository.findAll().stream().map(agenceMapper::toResponse).toList();
    }

    @Override
    public AgenceDto.Response update(Long id, AgenceDto.Request request) {
        log.info("Mise à jour de l'agence avec l'ID: {}", id);
        Agence agence = getEntityById(id);
        agenceMapper.updateEntity(request, agence);
        return agenceMapper.toResponse(agenceRepository.save(agence));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de l'agence avec l'ID: {}", id);
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        if (vehiculeRepository.existsByAgence_IdAgence(id)
                || employeRepository.existsByAgence_IdAgence(id)) {
            throw new ResourceConflictException(
                    "Impossible de supprimer cette agence : elle est encore associée à des véhicules ou des employés.");
        }
        agenceRepository.deleteById(id);
    }

    private Agence getEntityById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }
}