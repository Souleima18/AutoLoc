package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IVehiculeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        log.info("Création d'un nouveau véhicule");
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        if (vehicule.getTarifJournalier() != null && vehicule.getTarifJournalier().doubleValue() < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        log.info("Recherche du véhicule avec l'ID: {}", id);
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    public List<Vehicule> findAll() {
        log.info("Récupération de tous les véhicules");
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        log.info("Mise à jour du véhicule avec l'ID: {}", id);
        Vehicule existant = findById(id);

        if (vehicule.getTarifJournalier() != null) {
            if (vehicule.getTarifJournalier().doubleValue() < 0) {
                throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
            }
            existant.setTarifJournalier(vehicule.getTarifJournalier());
        }
        if (vehicule.getStatut() != null) {
            existant.setStatut(vehicule.getStatut());
        }
        if (vehicule.getMarque() != null && !vehicule.getMarque().isBlank()) {
            existant.setMarque(vehicule.getMarque());
        }
        if (vehicule.getModele() != null && !vehicule.getModele().isBlank()) {
            existant.setModele(vehicule.getModele());
        }

        return vehiculeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du véhicule avec l'ID: {}", id);
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }
}