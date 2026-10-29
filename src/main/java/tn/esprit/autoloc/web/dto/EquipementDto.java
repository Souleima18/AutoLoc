package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class EquipementDto {

    private EquipementDto() {
    }

    public record Request(@NotBlank @Size(max = 100) String libelle) {
    }

    public record Response(Long idEquipement, String libelle) {
    }
}
