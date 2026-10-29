package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.EquipementDto;

public interface IEquipementService {
    EquipementDto.Response create(EquipementDto.Request equipement);
    EquipementDto.Response findById(Long id);
    List<EquipementDto.Response> findAll();
    EquipementDto.Response update(Long id, EquipementDto.Request equipement);
    void deleteById(Long id);
}