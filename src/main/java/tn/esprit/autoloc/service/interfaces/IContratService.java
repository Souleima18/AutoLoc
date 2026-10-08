package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Contrat;
import java.util.List;
import java.util.Optional;

public interface IContratService {

    // CREATE
    Contrat createContrat(Contrat contrat);

    // READ
    Optional<Contrat> getContratById(Long id);
    List<Contrat> getAllContrats();
    List<Contrat> getContratsByValide(Boolean valide);

    // UPDATE
    Contrat updateContrat(Long id, Contrat contrat);
    Contrat validateContrat(Long id);

    // DELETE
    void deleteContrat(Long id);
    void deleteAllContrats();

    // UTILITY
    boolean contratExists(Long id);
    long countContrats();
    long countContratsByValide(Boolean valide);
}