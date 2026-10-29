package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tn.esprit.autoloc.domain.RoleEmploye;

public final class EmployeDto {

    private EmployeDto() {
    }

    public record Request(
            @NotBlank @Size(max = 50) String nom,
            @NotBlank @Size(max = 50) String prenom,
            @NotNull RoleEmploye role,
            @NotNull @Positive Long agenceId) {
    }

    public record Response(Long idEmploye, String nom, String prenom, RoleEmploye role, Long agenceId) {
    }
}
