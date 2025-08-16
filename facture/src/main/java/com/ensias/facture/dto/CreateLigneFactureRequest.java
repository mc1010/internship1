package com.ensias.facture.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateLigneFactureRequest(
        @NotNull(message = "Le produit est obligatoire")
        Long produitId,

        @Min(value = 1, message = "La quantité doit être au moins 1")
        Integer quantite
) {
}
