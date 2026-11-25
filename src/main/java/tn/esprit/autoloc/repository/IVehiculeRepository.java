package tn.esprit.autoloc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
    boolean existsByAgence_IdAgence(Long agenceId);
    Optional<Vehicule> findByImmatriculationIgnoreCase(String immatriculation);
}