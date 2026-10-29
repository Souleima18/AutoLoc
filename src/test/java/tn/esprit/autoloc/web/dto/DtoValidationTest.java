package tn.esprit.autoloc.web.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

class DtoValidationTest {

    @Test
    void clientRequestRejectsInvalidEmailAndPhone() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            ClientDto.Request request = new ClientDto.Request(
                    "Martin", "Amira", "adresse-invalide", "telephone-invalide",
                    "PERMIS-12345", null);

            var violations = validator.validate(request);

            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telephone")));
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateInscription")));
        }
    }

    @Test
    void vehiculeRequestRejectsNegativePriceAndMissingAgency() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            VehiculeDto.Request request = new VehiculeDto.Request(
                    "TN-123-XYZ", "Toyota", "Yaris", CategorieVehicule.CITADINE,
                    BigDecimal.valueOf(-1), StatutVehicule.DISPONIBLE, null);

            var violations = validator.validate(request);

            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tarifJournalier")));
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("agenceId")));
        }
    }
}
