package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IVehiculeService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    // ============ CREATE ============
    @Override
    public Vehicule createVehicule(Vehicule vehicule) {
        log.info("Creating vehicule: {} {}", vehicule.getMarque(), vehicule.getModele());

        if (vehiculeExistsByImmatriculation(vehicule.getImmatriculation())) {
            throw new RuntimeException("Vehicule with immatriculation " + vehicule.getImmatriculation() + " already exists");
        }

        return vehiculeRepository.save(vehicule);
    }

    // ============ READ ============
    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicule> getVehiculeById(Long id) {
        log.info("Fetching vehicule with id: {}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid vehicule id: {}", id);
            return Optional.empty();
        }

        return vehiculeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> getAllVehicules() {
        log.info("Fetching all vehicules");
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        log.info("Found {} vehicules", vehicules.size());
        return vehicules;
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule getVehiculeByImmatriculation(String immatriculation) {
        log.info("Fetching vehicule with immatriculation: {}", immatriculation);

        return vehiculeRepository.findAll().stream()
                .filter(v -> v.getImmatriculation().equals(immatriculation))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vehicule not found with immatriculation: " + immatriculation));
    }

    // ============ UPDATE ============
    @Override
    public Vehicule updateVehicule(Long id, Vehicule vehicule) {
        log.info("Updating vehicule with id: {}", id);

        Vehicule existingVehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicule not found with id: " + id));

        // Vérifier si la nouvelle immatriculation n'existe pas déjà
        if (!existingVehicule.getImmatriculation().equals(vehicule.getImmatriculation()) &&
                vehiculeExistsByImmatriculation(vehicule.getImmatriculation())) {
            throw new RuntimeException("Another vehicule already exists with immatriculation: " + vehicule.getImmatriculation());
        }

        existingVehicule.setImmatriculation(vehicule.getImmatriculation());
        existingVehicule.setMarque(vehicule.getMarque());
        existingVehicule.setModele(vehicule.getModele());
        existingVehicule.setCategorie(vehicule.getCategorie());
        existingVehicule.setTarifJournalier(vehicule.getTarifJournalier());
        existingVehicule.setStatut(vehicule.getStatut());

        log.info("Vehicule updated successfully");
        return vehiculeRepository.save(existingVehicule);
    }

    // ============ DELETE ============
    @Override
    public void deleteVehicule(Long id) {
        log.info("Deleting vehicule with id: {}", id);

        if (!vehiculeExists(id)) {
            log.warn("Vehicule not found with id: {}", id);
            throw new RuntimeException("Vehicule not found with id: " + id);
        }

        vehiculeRepository.deleteById(id);
        log.info("Vehicule deleted successfully");
    }

    @Override
    public void deleteAllVehicules() {
        log.warn("Deleting ALL vehicules");
        vehiculeRepository.deleteAll();
        log.info("All vehicules deleted");
    }

    // ============ UTILITY ============
    @Override
    @Transactional(readOnly = true)
    public boolean vehiculeExists(Long id) {
        return vehiculeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean vehiculeExistsByImmatriculation(String immatriculation) {
        return vehiculeRepository.findAll().stream()
                .anyMatch(v -> v.getImmatriculation().equals(immatriculation));
    }

    @Override
    @Transactional(readOnly = true)
    public long countVehicules() {
        long count = vehiculeRepository.count();
        log.info("Total vehicules in database: {}", count);
        return count;
    }
}