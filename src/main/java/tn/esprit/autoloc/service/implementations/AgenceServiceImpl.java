package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IAgenceService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;

    @Override
    public Agence create(Agence agence) {
        log.info("Création d'une nouvelle agence");
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        if (agence.getNom() == null || agence.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        log.info("Recherche de l'agence avec l'ID: {}", id);
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    public List<Agence> findAll() {
        log.info("Récupération de toutes les agences");
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        log.info("Mise à jour de l'agence avec l'ID: {}", id);
        Agence existant = findById(id);

        if (agence.getNom() != null && !agence.getNom().isBlank()) {
            existant.setNom(agence.getNom());
        }
        if (agence.getAdresse() != null && !agence.getAdresse().isBlank()) {
            existant.setAdresse(agence.getAdresse());
        }
        if (agence.getTelephone() != null && !agence.getTelephone().isBlank()) {
            existant.setTelephone(agence.getTelephone());
        }
        if (agence.getVille() != null && !agence.getVille().isBlank()) {
            existant.setVille(agence.getVille());
        }

        return agenceRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de l'agence avec l'ID: {}", id);
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        if (vehiculeRepository.existsByAgence_IdAgence(id)
                || employeRepository.existsByAgence_IdAgence(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de supprimer cette agence : elle est encore associée à des véhicules ou des employés."
            );
        }
        agenceRepository.deleteById(id);
    }
}