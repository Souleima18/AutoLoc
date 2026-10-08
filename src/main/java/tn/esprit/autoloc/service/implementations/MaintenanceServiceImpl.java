package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.service.interfaces.IMaintenanceService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance createMaintenance(Maintenance maintenance) {
        log.info("Creating maintenance from {} to {}", maintenance.getDateDebut(), maintenance.getDateFin());
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Maintenance> getMaintenanceById(Long id) {
        log.info("Fetching maintenance with id: {}", id);
        return maintenanceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> getAllMaintenances() {
        log.info("Fetching all maintenances");
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance updateMaintenance(Long id, Maintenance maintenance) {
        log.info("Updating maintenance with id: {}", id);
        Maintenance existingMaintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        existingMaintenance.setDateDebut(maintenance.getDateDebut());
        existingMaintenance.setDateFin(maintenance.getDateFin());
        existingMaintenance.setDescription(maintenance.getDescription());

        return maintenanceRepository.save(existingMaintenance);
    }

    @Override
    public void deleteMaintenance(Long id) {
        log.info("Deleting maintenance with id: {}", id);
        maintenanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean maintenanceExists(Long id) {
        return maintenanceRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countMaintenances() {
        return maintenanceRepository.count();
    }
}