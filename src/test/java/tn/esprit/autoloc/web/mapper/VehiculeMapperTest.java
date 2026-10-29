package tn.esprit.autoloc.web.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;

class VehiculeMapperTest {

    private final VehiculeMapper mapper = Mappers.getMapper(VehiculeMapper.class);

    @Test
    void responseContainsRelatedAgencyIdWithoutExposingJpaRelations() {
        Agence agence = new Agence();
        agence.setIdAgence(42L);
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(7L);
        vehicule.setImmatriculation("TN-123-XYZ");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(BigDecimal.valueOf(95));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        vehicule.setAgence(agence);

        var response = mapper.toResponse(vehicule);

        assertEquals(7L, response.idVehicule());
        assertEquals(42L, response.agenceId());
        assertEquals("TN-123-XYZ", response.immatriculation());
    }
}
