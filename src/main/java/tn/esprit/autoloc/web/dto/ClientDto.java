package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public final class ClientDto {

    private ClientDto() {
    }

    public record Request(
            @NotBlank @Size(max = 50) String nom,
            @NotBlank @Size(max = 50) String prenom,
            @NotBlank @Email @Size(max = 100) String email,
            @NotBlank @Size(max = 20)
            @Pattern(regexp = "^\\+?[0-9][0-9\\s.-]{5,18}$", message = "Format de téléphone invalide")
            String telephone,
            @NotBlank @Size(max = 20) String numPermis,
            @NotNull LocalDate dateInscription) {
    }

    public record Response(
            Long idClient,
            String nom,
            String prenom,
            String email,
            String telephone,
            String numPermis,
            LocalDate dateInscription) {
    }
}
