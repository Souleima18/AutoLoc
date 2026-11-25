package tn.esprit.autoloc.web.dto.affectation;

import java.math.BigDecimal;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

public record VehiculeDTO(
        Long idVehicule,
        String immatriculation,
        String marque,
        String modele,
        CategorieVehicule categorie,
        BigDecimal tarifJournalier,
        StatutVehicule statut,
        Long agenceId) {
}
