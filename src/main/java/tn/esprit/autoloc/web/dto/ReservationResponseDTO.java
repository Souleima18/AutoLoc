package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;
import tn.esprit.autoloc.domain.StatutReservation;

public record ReservationResponseDTO(
        Long idReservation,
        LocalDate dateDebut,
        LocalDate dateFin,
        StatutReservation statut,
        Long vehiculeId,
        Long clientId,
        Long contratId) {
}
