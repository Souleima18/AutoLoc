package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class ContratDto {

    private ContratDto() {
    }

    public record Request(
            @NotNull LocalDate dateSignature,
            @NotNull @DecimalMin("0.00") BigDecimal montantTotal,
            @NotNull Boolean valide) {
    }

    public record Response(Long idContrat, LocalDate dateSignature, BigDecimal montantTotal, Boolean valide) {
    }
}
