package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Maintenance;
import java.util.List;
import java.util.Optional;

public interface IMaintenanceService {

    // CREATE
    Maintenance createMaintenance(Maintenance maintenance);

    // READ
    Optional<Maintenance> getMaintenanceById(Long id);
    List<Maintenance> getAllMaintenances();

    // UPDATE
    Maintenance updateMaintenance(Long id, Maintenance maintenance);

    // DELETE
    void deleteMaintenance(Long id);
    boolean maintenanceExists(Long id);
    long countMaintenances();
}