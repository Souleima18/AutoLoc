package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.StatutReservation;

public record ReservationWithContratRequestDTO(
        @NotNull LocalDate dateSignature,
        @NotNull LocalDate dateDebut,
        @NotNull LocalDate dateFin,
        @NotNull @Positive Long idVehicule,
        @NotNull @Positive Long idClient,
        @NotNull @DecimalMin("0.00") BigDecimal montantTotal,
        @NotNull StatutReservation statut) {
}
