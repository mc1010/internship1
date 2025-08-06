package com.ensias.facture.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProduitRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        String description,

        @NotNull(message = "Le prix unitaire est obligatoire")
        @Positive(message = "Le prix unitaire doit être positif")
        Double prixUnitaire,

        @NotBlank(message = "L’unité de mesure est obligatoire")
        String uniteMesure,

        @NotNull(message = "La TVA est obligatoire")
        @PositiveOrZero(message = "La TVA doit être positive ou nulle")
        Double tva


) {

}
