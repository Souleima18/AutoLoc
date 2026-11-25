package tn.esprit.autoloc.service.implementations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IVehiculeService;
import tn.esprit.autoloc.web.dto.affectation.VehiculeDTO;
import tn.esprit.autoloc.web.dto.VehiculeDto;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeDto.Response create(VehiculeDto.Request request) {
        log.info("Création d'un nouveau véhicule");
        Vehicule vehicule = vehiculeMapper.toEntity(request);
        vehicule.setAgence(getAgence(request.agenceId()));
        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    public VehiculeDto.Response findById(Long id) {
        log.info("Recherche du véhicule avec l'ID: {}", id);
        return vehiculeMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<VehiculeDto.Response> findAll() {
        log.info("Récupération de tous les véhicules");
        return vehiculeRepository.findAll().stream().map(vehiculeMapper::toResponse).toList();
    }

    @Override
    public VehiculeDto.Response update(Long id, VehiculeDto.Request request) {
        log.info("Mise à jour du véhicule avec l'ID: {}", id);
        Vehicule vehicule = getEntityById(id);
        vehiculeMapper.updateEntity(request, vehicule);
        vehicule.setAgence(getAgence(request.agenceId()));
        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du véhicule avec l'ID: {}", id);
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = getEntityById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));

        if (vehicule.getEquipements() == null) {
            vehicule.setEquipements(new HashSet<>());
        }
        vehicule.getEquipements().add(equipement);
        return toVehiculeDTO(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("La description de la maintenance est obligatoire.");
        }
        if (immatriculation == null || immatriculation.isBlank()) {
            throw new IllegalArgumentException("L'immatriculation du véhicule est obligatoire.");
        }
        Vehicule vehicule = vehiculeRepository.findByImmatriculationIgnoreCase(immatriculation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucun véhicule trouvé avec l'immatriculation : " + immatriculation));

        LocalDate today = LocalDate.now();
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(today);
        maintenance.setDateFin(today);
        maintenance.setDescription(description.trim());
        maintenance.setVehicule(vehicule);
        maintenanceRepository.save(maintenance);

        vehicule.setStatut(StatutVehicule.MAINTENANCE);
        return toVehiculeDTO(vehiculeRepository.save(vehicule));
    }

    private Vehicule getEntityById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    private Agence getAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    private VehiculeDTO toVehiculeDTO(Vehicule vehicule) {
        VehiculeDto.Response response = vehiculeMapper.toResponse(vehicule);
        return new VehiculeDTO(
                response.idVehicule(),
                response.immatriculation(),
                response.marque(),
                response.modele(),
                response.categorie(),
                response.tarifJournalier(),
                response.statut(),
                response.agenceId());
    }
}
