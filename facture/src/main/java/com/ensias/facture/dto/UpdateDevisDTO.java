package com.ensias.facture.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UpdateDevisDTO(
        @NotNull(message = "La date d'expiration est obligatoire")
        LocalDate dateExpiration,
        String conditions,
        List<UpdateLigneDevisDTO> lignes
) {
}
