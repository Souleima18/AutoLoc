package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.ModePaiement;

public final class PaiementDto {

    private PaiementDto() {
    }

    public record Response(
            Long idPaiement,
            BigDecimal montant,
            LocalDate datePaiement,
            ModePaiement modePaiement,
            Long contratId) {
    }
}
