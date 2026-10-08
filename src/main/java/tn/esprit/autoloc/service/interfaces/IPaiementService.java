package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Paiement;
import java.util.List;
import java.util.Optional;

public interface IPaiementService {

    // CREATE
    Paiement createPaiement(Paiement paiement);

    // READ
    Optional<Paiement> getPaiementById(Long id);
    List<Paiement> getAllPaiements();

    // UPDATE
    Paiement updatePaiement(Long id, Paiement paiement);

    // DELETE
    void deletePaiement(Long id);
    boolean paiementExists(Long id);
    long countPaiements();
}