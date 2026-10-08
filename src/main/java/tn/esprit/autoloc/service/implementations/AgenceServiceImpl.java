package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.interfaces.IAgenceService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence createAgence(Agence agence) {
        log.info("Creating agence: {}", agence.getNom());
        return agenceRepository.save(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agence> getAgenceById(Long id) {
        log.info("Fetching agence with id: {}", id);
        return agenceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> getAllAgences() {
        log.info("Fetching all agences");
        return agenceRepository.findAll();
    }

    @Override
    public Agence updateAgence(Long id, Agence agence) {
        log.info("Updating agence with id: {}", id);
        Agence existingAgence = agenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence not found with id: " + id));

        existingAgence.setNom(agence.getNom());
        existingAgence.setVille(agence.getVille());
        existingAgence.setAdresse(agence.getAdresse());
        existingAgence.setTelephone(agence.getTelephone());

        return agenceRepository.save(existingAgence);
    }

    @Override
    public void deleteAgence(Long id) {
        log.info("Deleting agence with id: {}", id);
        agenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean agenceExists(Long id) {
        return agenceRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAgences() {
        return agenceRepository.count();
    }
}