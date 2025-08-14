package com.ensias.facture.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateLigneDevisRequest(
        @NotNull(message = "ProduitId est obligatoire")
        Long produitId,
        @NotNull(message = "Quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être au moins 1")
        Integer quantite
) {
}
