package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public final class AgenceDto {

    private AgenceDto() {
    }

    public record Request(
            @NotBlank @Size(max = 100) String nom,
            @NotBlank @Size(max = 100) String ville,
            @NotBlank @Size(max = 200) String adresse,
            @NotBlank @Size(max = 20)
            @Pattern(regexp = "^\\+?[0-9][0-9\\s.-]{5,18}$", message = "Format de téléphone invalide")
            String telephone) {
    }

    public record Response(Long idAgence, String nom, String ville, String adresse, String telephone) {
    }
}
