package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Vehicule;
import java.util.List;
import java.util.Optional;

public interface IVehiculeService {

    // CREATE
    Vehicule createVehicule(Vehicule vehicule);

    // READ
    Optional<Vehicule> getVehiculeById(Long id);
    List<Vehicule> getAllVehicules();
    Vehicule getVehiculeByImmatriculation(String immatriculation);

    // UPDATE
    Vehicule updateVehicule(Long id, Vehicule vehicule);

    // DELETE
    void deleteVehicule(Long id);
    void deleteAllVehicules();

    // UTILITY
    boolean vehiculeExists(Long id);
    boolean vehiculeExistsByImmatriculation(String immatriculation);
    long countVehicules();
}