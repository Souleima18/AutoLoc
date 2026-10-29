package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.StatutReservation;

public final class ReservationDto {

    private ReservationDto() {
    }

    public record Request(
            @NotNull LocalDate dateDebut,
            @NotNull LocalDate dateFin,
            @NotNull StatutReservation statut,
            @NotNull @Positive Long vehiculeId,
            @NotNull @Positive Long clientId,
            @NotNull @Positive Long contratId) {
    }

    public record Response(
            Long idReservation,
            LocalDate dateDebut,
            LocalDate dateFin,
            StatutReservation statut,
            Long vehiculeId,
            Long clientId,
            Long contratId) {
    }
}
