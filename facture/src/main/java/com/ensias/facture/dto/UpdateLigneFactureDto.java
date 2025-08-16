package com.ensias.facture.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateLigneFactureDto(
        Long id,
        @Min(value = 1, message = "La quantité doit être au moins 1")
        int quantite
) {
}
