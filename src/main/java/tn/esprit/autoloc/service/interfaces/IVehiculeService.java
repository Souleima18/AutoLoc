package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.VehiculeDto;
import tn.esprit.autoloc.web.dto.affectation.VehiculeDTO;

public interface IVehiculeService {
    VehiculeDto.Response create(VehiculeDto.Request vehicule);
    VehiculeDto.Response findById(Long id);
    List<VehiculeDto.Response> findAll();
    VehiculeDto.Response update(Long id, VehiculeDto.Request vehicule);
    void deleteById(Long id);
    VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);
    VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation);
}