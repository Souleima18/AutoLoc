package tn.esprit.autoloc.service.implementations;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceDto;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDto.Response create(MaintenanceDto.Request request) {
        log.info("Création d'une nouvelle maintenance");
        validateDates(request.dateDebut(), request.dateFin());
        Maintenance maintenance = maintenanceMapper.toEntity(request);
        maintenance.setVehicule(getVehicule(request.vehiculeId()));
        return maintenanceMapper.toResponse(maintenanceRepository.save(maintenance));
    }

    @Override
    public MaintenanceDto.Response findById(Long id) {
        log.info("Recherche de la maintenance avec l'ID: {}", id);
        return maintenanceMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<MaintenanceDto.Response> findAll() {
        log.info("Récupération de toutes les maintenances");
        return maintenanceRepository.findAll().stream().map(maintenanceMapper::toResponse).toList();
    }

    @Override
    public MaintenanceDto.Response update(Long id, MaintenanceDto.Request request) {
        log.info("Mise à jour de la maintenance avec l'ID: {}", id);
        validateDates(request.dateDebut(), request.dateFin());
        Maintenance maintenance = getEntityById(id);
        maintenanceMapper.updateEntity(request, maintenance);
        maintenance.setVehicule(getVehicule(request.vehiculeId()));
        return maintenanceMapper.toResponse(maintenanceRepository.save(maintenance));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de la maintenance avec l'ID: {}", id);
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }

    private Maintenance getEntityById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    private Vehicule getVehicule(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }
}
