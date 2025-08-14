package com.ensias.facture.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateDevisDTO(

        @NotNull(message = "La date d'expiration est obligatoire")
        LocalDate dateExpiration,
        @NotNull(message = "Le clientId est obligatoire")
        Integer clientId,
        @NotNull(message="Veuillez mentionner les conditions")
        String conditions,

        List<CreateLigneDevisRequest> lignes
) {
}
