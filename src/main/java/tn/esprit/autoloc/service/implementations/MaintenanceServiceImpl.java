package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.service.interfaces.IMaintenanceService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        log.info("Création d'une nouvelle maintenance");
        if (maintenance.getIdMaintenance() != null) {
            throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        if (maintenance.getDateDebut() != null && maintenance.getDateFin() != null) {
            if (maintenance.getDateFin().isBefore(maintenance.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance findById(Long id) {
        log.info("Recherche de la maintenance avec l'ID: {}", id);
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public List<Maintenance> findAll() {
        log.info("Récupération de toutes les maintenances");
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        log.info("Mise à jour de la maintenance avec l'ID: {}", id);
        Maintenance existant = findById(id);

        if (maintenance.getDateDebut() != null) {
            existant.setDateDebut(maintenance.getDateDebut());
        }
        if (maintenance.getDateFin() != null) {
            if (maintenance.getDateFin().isBefore(existant.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
            existant.setDateFin(maintenance.getDateFin());
        }
        if (maintenance.getDescription() != null && !maintenance.getDescription().isBlank()) {
            existant.setDescription(maintenance.getDescription());
        }

        return maintenanceRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de la maintenance avec l'ID: {}", id);
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }
}