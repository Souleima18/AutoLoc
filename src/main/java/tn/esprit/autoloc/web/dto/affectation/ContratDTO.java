package tn.esprit.autoloc.web.dto.affectation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratDTO(
        Long idContrat,
        LocalDate dateSignature,
        BigDecimal montantTotal,
        Boolean valide) {
}
