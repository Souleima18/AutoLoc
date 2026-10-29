package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.VehiculeDto;

public interface IVehiculeService {
    VehiculeDto.Response create(VehiculeDto.Request vehicule);
    VehiculeDto.Response findById(Long id);
    List<VehiculeDto.Response> findAll();
    VehiculeDto.Response update(Long id, VehiculeDto.Request vehicule);
    void deleteById(Long id);
}