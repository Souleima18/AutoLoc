package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Agence;
import java.util.List;
import java.util.Optional;

public interface IAgenceService {

    // CREATE
    Agence createAgence(Agence agence);

    // READ
    Optional<Agence> getAgenceById(Long id);
    List<Agence> getAllAgences();

    // UPDATE
    Agence updateAgence(Long id, Agence agence);

    // DELETE
    void deleteAgence(Long id);
    boolean agenceExists(Long id);
    long countAgences();
}