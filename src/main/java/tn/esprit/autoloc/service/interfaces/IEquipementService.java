package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Equipement;
import java.util.List;
import java.util.Optional;

public interface IEquipementService {

    // CREATE
    Equipement createEquipement(Equipement equipement);

    // READ
    Optional<Equipement> getEquipementById(Long id);
    List<Equipement> getAllEquipements();

    // UPDATE
    Equipement updateEquipement(Long id, Equipement equipement);

    // DELETE
    void deleteEquipement(Long id);
    boolean equipementExists(Long id);
    long countEquipements();
}